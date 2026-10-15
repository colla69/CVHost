#!/usr/bin/env python3
"""Search Google Jobs through SerpApi and print compact JSON for job-scout.

Google Jobs indexes LinkedIn, StepStone, XING, Indeed and employer ads, so this is how the
search reaches LinkedIn without scraping it.

    python3 jobs/tools/google_jobs.py --check            # key found? searches left this month?
    python3 jobs/tools/google_jobs.py "senior vue developer remote"  [more queries...]

The key comes from the SERPAPI_KEY environment variable, else from SERPAPI_KEY=... in the
repo-root .env (gitignored). It is never printed. One query costs one search; the free plan
has 250 a month, shared by every run, so this script refuses to go past MONTHLY_BUDGET.
"""
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from datetime import datetime, timezone
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
RUNS = ROOT / 'jobs' / 'runs'
SOURCE = 'google-jobs'
MONTHLY_BUDGET = 240  # of the free plan's 250, leaving room for manual tests
ENDPOINT = 'https://serpapi.com/search.json'
TIMEOUT = 120  # seconds; 4 of 8 searches hit the old 60 s limit on 2026-10-15


def api_key():
    key = os.environ.get('SERPAPI_KEY', '').strip()
    if key:
        return key, 'environment'
    env = ROOT / '.env'
    if env.exists():
        for line in env.read_text().splitlines():
            line = line.strip()
            if line.startswith('export '):
                line = line[len('export '):]
            if line.startswith('SERPAPI_KEY='):
                key = line.split('=', 1)[1].strip().strip('"\'')
                if key:
                    return key, '.env'
    return None, None


def used_this_month():
    """Searches recorded by committed or pending sweeps this calendar month (UTC)."""
    month = datetime.now(timezone.utc).strftime('%Y-%m')
    used = 0
    for path in RUNS.glob(f'{month}-*.json'):
        try:
            run = json.loads(path.read_text())
        except (OSError, ValueError):
            continue
        for source in run.get('sources', []):
            if source.get('name') == SOURCE:
                used += len(source.get('queries', []))
    return used


def search(key, query):
    # Without `location` SerpApi may search from its proxy's location, which does not match gl=de;
    # on 2026-10-15 that returned nothing for every query.
    params = {'engine': 'google_jobs', 'q': query, 'location': 'Germany', 'gl': 'de', 'hl': 'en',
              'api_key': key}
    try:
        with urllib.request.urlopen(f'{ENDPOINT}?{urllib.parse.urlencode(params)}', timeout=TIMEOUT) as r:
            data = json.load(r)
    except urllib.error.HTTPError as e:
        return {'query': query, 'error': f'HTTP {e.code}'}
    except (urllib.error.URLError, TimeoutError, ValueError) as e:
        # str(e) never carries the URL, so the key cannot leak through it
        return {'query': query, 'error': type(e).__name__ + ': ' + str(getattr(e, 'reason', e))}
    if 'error' in data:
        return {'query': query, 'error': data['error']}
    jobs = []
    for j in data.get('jobs_results', []):
        ext = j.get('detected_extensions', {})
        jobs.append({
            'title': j.get('title'),
            'company': j.get('company_name'),
            'location': j.get('location'),
            'via': j.get('via'),
            'posted': ext.get('posted_at'),
            'schedule': ext.get('schedule_type'),
            'remote': ext.get('work_from_home', False),
            'salary': ext.get('salary'),
            'apply': [{'title': a.get('title'), 'link': a.get('link')} for a in j.get('apply_options', [])],
            'description': (j.get('description') or '')[:1500],
        })
    return {'query': query, 'count': len(jobs), 'jobs': jobs}


def main(argv):
    key, origin = api_key()
    used = used_this_month()
    left = MONTHLY_BUDGET - used
    if not argv or argv == ['--check']:
        print(json.dumps({'key': f'found ({origin})' if key else 'missing',
                          'used_this_month': used, 'budget': MONTHLY_BUDGET, 'left': left}))
        return 0 if key else 1
    if not key:
        print(json.dumps({'error': 'SERPAPI_KEY missing from environment and .env'}))
        return 1
    if len(argv) > left:
        print(json.dumps({'error': f'{len(argv)} queries but only {left} of {MONTHLY_BUDGET} left this month'}))
        return 1
    print(json.dumps([search(key, q) for q in argv], ensure_ascii=False, indent=1))
    return 0


if __name__ == '__main__':
    sys.exit(main(sys.argv[1:]))
