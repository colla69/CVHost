#!/usr/bin/env python3
"""Validate job-scout sweeps, write their reports and rebuild the postings index.

    python3 jobs/tools/ingest.py jobs/runs/<run_id>.json   # a new sweep: normalise, validate, report, index
    python3 jobs/tools/ingest.py                           # check every sweep, rebuild reports and index

A new sweep is rewritten in place once: every posting gets its canonical id and every object a fixed key
order. Sweeps already committed to git are history and are never rewritten; the script refuses to.
Standard library only, so it runs unchanged on this machine and in a cloud routine.
"""
import json
import re
import subprocess
import sys
import unicodedata
from pathlib import Path

JOBS = Path(__file__).resolve().parent.parent
RUNS = JOBS / 'runs'
INDEX = JOBS / 'postings.jsonl'
VOCAB = json.loads((JOBS / 'vocab.json').read_text())
SCHEMA = 3
READABLE = (1, 2, 3)

RUN_ID = re.compile(r'^\d{4}-\d{2}-\d{2}T\d{4}Z$')
COUNTRY = re.compile(r'^[A-Z]{2}$')
LANGUAGE = re.compile(r'^[a-z]{2}$')
DATE = re.compile(r'^\d{4}-\d{2}-\d{2}')
LEGAL_SUFFIX = re.compile(r'-(gmbh|mbh|ag|se|kg|kgaa|co|ltd|limited|inc|llc|bv|nv|sas|sa|srl|spa|plc|oy|ab|as|aps)$')
GENDER_TAG = re.compile(r'\(\s*(?:[mwfdx]\s*/\s*)+[mwfdx]\s*\)|\(\s*all genders?\s*\)|\(\s*gn\s*\)', re.I)

RUN_KEYS = ('schema', 'run_id', 'started_at', 'finished_at', 'trigger', 'agent_sha', 'profile_sha',
            'summary', 'open_questions', 'sources', 'postings')
SOURCE_KEYS = ('name', 'method', 'queries', 'results', 'evaluated', 'status', 'note')
POSTING_KEYS = ('id', 'url', 'source', 'company', 'title', 'lane', 'contract', 'seniority', 'remote',
                'salary', 'languages', 'stack', 'domain', 'posted_at', 'verified', 'verdict',
                'drop_reason', 'score', 'why', 'gap')
POSTING_KEYS_V2 = POSTING_KEYS + ('pay_estimate',)
POSTING_KEYS_V3 = POSTING_KEYS_V2 + ('link_check',)
LINK_CHECK_KEYS = ('checked_at', 'method', 'result', 'evidence')
# A link counts as checked only through a live answer: the HTTP status itself, or the employer's ATS
# listing or API showing the posting as published. A fetched page (WebFetch) can be a stale cached copy:
# on 2026-10-14 it showed Recare's role as open while the real page was a 404.
SHORTLIST_CHECKS = ('http', 'api')
REMOTE_KEYS = ('type', 'countries', 'timezone', 'onsite_days_per_quarter', 'eor')
SALARY_KEYS = ('stated', 'min', 'max', 'currency', 'period')
SCORE_KEYS = ('stack', 'seniority', 'remote', 'domain', 'company', 'total')
ESTIMATE_KEYS = ('point', 'min', 'max', 'currency', 'period', 'confidence', 'basis')
# The owner's floor (profile.md, decided 2026-10-14): EUR 60k a year, or a freelance rate that adds up to it
# over 200 billable days. Anything projected to pay less is dropped as low-pay, never kept.
PAY_FLOOR = {'year': 60000, 'month': 5000, 'day': 300, 'hour': 38}
GERMANY_OK = {'DE', 'EU', 'EEA', 'EUROPE', 'DACH', 'EMEA', 'WORLDWIDE'}


def slug(text):
    text = unicodedata.normalize('NFKD', text).encode('ascii', 'ignore').decode()
    return re.sub(r'[^a-z0-9]+', '-', text.lower()).strip('-')


def posting_id(company, title):
    """Stable across reposts: legal suffixes and German gender tags do not change the id."""
    company_part = slug(company)
    while LEGAL_SUFFIX.search(company_part):
        company_part = LEGAL_SUFFIX.sub('', company_part)
    return f'{company_part}--{slug(GENDER_TAG.sub(" ", title))}'


def ordered(obj, keys):
    """Known keys first, in schema order; anything unknown after, so validation can name it."""
    return {**{k: obj[k] for k in keys if k in obj}, **{k: v for k, v in obj.items() if k not in keys}}


def posting_keys(run):
    schema = run.get('schema', SCHEMA)
    return POSTING_KEYS_V3 if schema >= 3 else POSTING_KEYS_V2 if schema >= 2 else POSTING_KEYS


def normalise(run):
    for posting in run.get('postings', []):
        if isinstance(posting.get('company'), str) and isinstance(posting.get('title'), str):
            posting['id'] = posting_id(posting['company'], posting['title'])
    run['sources'] = [ordered(s, SOURCE_KEYS) for s in run.get('sources', [])]
    postings = []
    for posting in run.get('postings', []):
        posting = ordered(posting, posting_keys(run))
        for key, keys in (('remote', REMOTE_KEYS), ('salary', SALARY_KEYS), ('score', SCORE_KEYS),
                          ('pay_estimate', ESTIMATE_KEYS), ('link_check', LINK_CHECK_KEYS)):
            if isinstance(posting.get(key), dict):
                posting[key] = ordered(posting[key], keys)
        postings.append(posting)
    run['postings'] = postings
    return ordered(run, RUN_KEYS)


def check(run, name):
    errors = []

    def err(message):
        errors.append(f'{name}: {message}')

    def keys(obj, required, where):
        missing = [k for k in required if k not in obj]
        extra = [k for k in obj if k not in required]
        if missing:
            err(f'{where}: missing {", ".join(missing)}')
        if extra:
            err(f'{where}: unknown {", ".join(extra)}')
        return not missing

    def enum(value, vocab, where):
        if value not in VOCAB[vocab]:
            err(f'{where}: {value!r} is not in vocab.json "{vocab}"')

    if not keys(run, RUN_KEYS, 'run'):
        return errors
    if run['schema'] not in READABLE:
        err(f'schema is {run["schema"]!r}, this script reads {", ".join(map(str, READABLE))}')
        return errors
    if not RUN_ID.match(str(run['run_id'])):
        err(f'run_id {run["run_id"]!r} is not YYYY-MM-DDTHHMMZ')
    if name != run['run_id']:
        err(f'file name does not match run_id {run["run_id"]!r}')
    enum(run['trigger'], 'trigger', 'trigger')
    if not isinstance(run['summary'], str) or not run['summary'].strip():
        err('summary must be a non-empty string')
    if not isinstance(run['open_questions'], list):
        err('open_questions must be a list')

    for i, source in enumerate(run['sources']):
        where = f'sources[{i}] {source.get("name")}'
        if not keys(source, SOURCE_KEYS, where):
            continue
        enum(source['method'], 'source_method', where)
        enum(source['status'], 'source_status', where)
        if not isinstance(source['queries'], list):
            err(f'{where}: queries must be a list')
        for field in ('results', 'evaluated'):
            if not isinstance(source[field], int) or source[field] < 0:
                err(f'{where}: {field} must be a count')

    seen = set()
    for i, p in enumerate(run['postings']):
        where = f'postings[{i}] {p.get("company")} / {p.get("title")}'
        if not keys(p, posting_keys(run), where):
            continue
        if p['id'] in seen:
            err(f'{where}: duplicate id {p["id"]} in this run; merge the two entries')
        seen.add(p['id'])
        if not str(p['url']).startswith('http'):
            err(f'{where}: url is not a link')
        enum(p['lane'], 'lane', where)
        enum(p['contract'], 'contract', where)
        enum(p['seniority'], 'seniority', where)
        enum(p['verdict'], 'verdict', where)

        remote = p['remote']
        if isinstance(remote, dict) and keys(remote, REMOTE_KEYS, f'{where} remote'):
            enum(remote['type'], 'remote_type', f'{where} remote')
            for place in remote['countries']:
                if not (COUNTRY.match(place) or place in VOCAB['regions']):
                    err(f'{where}: country {place!r} is neither ISO alpha-2 nor in vocab.json "regions"')
            onsite = remote['onsite_days_per_quarter']
            if onsite is not None and (not isinstance(onsite, (int, float)) or onsite < 0):
                err(f'{where}: onsite_days_per_quarter must be a number or null')
        elif not isinstance(remote, dict):
            err(f'{where}: remote must be an object')

        salary = p['salary']
        if not isinstance(salary, dict) or not isinstance(salary.get('stated'), bool):
            err(f'{where}: salary must be an object with a boolean "stated"')
        elif salary['stated'] and keys(salary, SALARY_KEYS, f'{where} salary'):
            enum(salary['currency'], 'currency', f'{where} salary')
            enum(salary['period'], 'salary_period', f'{where} salary')
            if salary['min'] is None and salary['max'] is None:
                err(f'{where}: salary is stated but has neither min nor max')

        for lang in p['languages']:
            if not LANGUAGE.match(lang):
                err(f'{where}: language {lang!r} is not ISO 639-1')
        for tag in p['stack']:
            enum(tag, 'stack', where)
        for tag in p['domain']:
            enum(tag, 'domain', where)
        if p['posted_at'] is not None and not DATE.match(str(p['posted_at'])):
            err(f'{where}: posted_at must be YYYY-MM-DD or null')
        if not isinstance(p['verified'], bool):
            err(f'{where}: verified must be true or false')

        if p['verdict'] == 'shortlisted':
            if p['verified'] is not True:
                err(f'{where}: an unverified posting can be kept, not shortlisted')
            if isinstance(remote, dict) and not GERMANY_OK & set(remote.get('countries') or []):
                err(f'{where}: shortlisted, but remote.countries does not show it is open to Germany')
        if run['schema'] >= 2:
            errors += check_estimate(p, where, name)
        if run['schema'] >= 3:
            errors += check_link(p, where, name, run)
        if p['verdict'] == 'dropped':
            enum(p['drop_reason'], 'drop_reason', where)
        elif p['drop_reason'] is not None:
            err(f'{where}: drop_reason set on a {p["verdict"]} posting')
        score = p['score']
        if score is None:
            if p['verdict'] != 'dropped':
                err(f'{where}: a {p["verdict"]} posting needs a score')
        elif isinstance(score, dict) and keys(score, SCORE_KEYS, f'{where} score'):
            parts = [score[k] for k in SCORE_KEYS[:-1]]
            if any(not isinstance(v, int) or not 0 <= v <= 2 for v in parts):
                err(f'{where}: each score criterion is an integer 0-2')
            elif score['total'] != sum(parts):
                err(f'{where}: score total {score["total"]} is not the sum {sum(parts)}')
        elif not isinstance(score, dict):
            err(f'{where}: score must be an object or null')
    return errors


def check_link(p, where, name, run):
    """Schema 3: a shortlisted posting carries proof, from this sweep, that its link is live."""
    lc = p['link_check']
    if lc is None:
        if p['verdict'] == 'shortlisted':
            return [f'{name}: {where}: shortlisted without a link_check']
        return []
    if not isinstance(lc, dict) or set(lc) != set(LINK_CHECK_KEYS):
        return [f'{name}: {where}: link_check needs exactly {", ".join(LINK_CHECK_KEYS)}']
    errors = []
    if lc['method'] not in VOCAB['link_check_method']:
        errors.append(f'{name}: {where}: link_check.method {lc["method"]!r} is not in vocab.json')
    if lc['result'] not in VOCAB['link_check_result']:
        errors.append(f'{name}: {where}: link_check.result {lc["result"]!r} is not in vocab.json')
    if not isinstance(lc['evidence'], str) or len(lc['evidence'].strip()) < 5:
        errors.append(f'{name}: {where}: link_check.evidence must quote what the check returned')
    if not str(lc['checked_at']).startswith(run['finished_at'][:10]) and \
            not str(lc['checked_at']).startswith(run['started_at'][:10]):
        errors.append(f'{name}: {where}: link_check.checked_at is not from this sweep')
    if p['verdict'] == 'shortlisted':
        if lc['result'] != 'live':
            errors.append(f'{name}: {where}: shortlisted, but its link check says {lc["result"]}')
        if lc['method'] not in SHORTLIST_CHECKS:
            errors.append(f'{name}: {where}: shortlisted on a {lc["method"]!r} check; only '
                          f'{" or ".join(SHORTLIST_CHECKS)} prove a link is live')
    if p['verdict'] != 'dropped' and lc['result'] == 'dead':
        errors.append(f'{name}: {where}: its link is dead; drop it as closed')
    return errors


def check_estimate(p, where, name):
    """Schema 2: every posting that is not dropped carries a sourced pay projection in EUR, at or above the floor."""
    errors = []
    est = p['pay_estimate']
    if est is None:
        if p['verdict'] != 'dropped':
            errors.append(f'{name}: {where}: a {p["verdict"]} posting needs a pay_estimate')
        return errors
    if not isinstance(est, dict) or set(est) != set(ESTIMATE_KEYS):
        return [f'{name}: {where}: pay_estimate needs exactly {", ".join(ESTIMATE_KEYS)}']
    nums = [est['point'], est['min'], est['max']]
    if any(not isinstance(v, (int, float)) for v in nums) or not est['min'] <= est['point'] <= est['max']:
        errors.append(f'{name}: {where}: pay_estimate needs numbers with min <= point <= max')
    if est['currency'] != 'EUR':
        errors.append(f'{name}: {where}: pay_estimate is projected in EUR; convert it')
    if est['period'] not in PAY_FLOOR:
        errors.append(f'{name}: {where}: pay_estimate period {est["period"]!r} is not one of {", ".join(PAY_FLOOR)}')
    if est['confidence'] not in VOCAB['confidence']:
        errors.append(f'{name}: {where}: confidence {est["confidence"]!r} is not in vocab.json "confidence"')
    if not isinstance(est['basis'], str) or len(est['basis'].strip()) < 10:
        errors.append(f'{name}: {where}: pay_estimate.basis must name where the figure comes from')
    if (not errors and p['verdict'] != 'dropped' and est['max'] < PAY_FLOOR[est['period']]):
        errors.append(f'{name}: {where}: projected at most {est["max"]:g} EUR/{est["period"]}, under the floor; '
                      'drop it as low-pay')
    return errors


def committed(path):
    try:
        result = subprocess.run(['git', 'ls-files', '--error-unmatch', str(path)], cwd=JOBS,
                                capture_output=True)
    except FileNotFoundError:
        return False
    return result.returncode == 0


def build_index(runs):
    index = {}
    for run in runs:
        for p in run['postings']:
            entry = index.setdefault(p['id'], {'first_seen': run['run_id'], 'seen_in': [], 'best_score': None})
            total = p['score']['total'] if p['score'] else None
            if total is not None and (entry['best_score'] is None or total > entry['best_score']):
                entry['best_score'] = total
            entry['seen_in'].append(run['run_id'])
            entry.update(company=p['company'], title=p['title'], url=p['url'], lane=p['lane'],
                         contract=p['contract'], last_seen=run['run_id'], last_verdict=p['verdict'])
    keys = ('id', 'company', 'title', 'url', 'lane', 'contract', 'first_seen', 'last_seen', 'seen_in',
            'last_verdict', 'best_score')
    rows = [{'id': pid, **entry} for pid, entry in index.items()]
    return [{k: row[k] for k in keys} for row in sorted(rows, key=lambda r: (r['first_seen'], r['id']))]


def cell(text):
    return str(text or '').replace('|', '\\|').replace('<', '&lt;').replace('\n', ' ').strip()


def link(p):
    label = cell(p['title']).replace('[', '(').replace(']', ')')
    target = p['url'].replace(' ', '%20').replace('(', '%28').replace(')', '%29')
    return f'[{label}]({target})'


def remote_text(remote):
    parts = [remote['type']]
    if remote['countries']:
        parts.append(', '.join(remote['countries']))
    if remote['onsite_days_per_quarter']:
        parts.append(f'{remote["onsite_days_per_quarter"]:g} d/qtr onsite')
    if remote['eor']:
        parts.append('EOR')
    return ' · '.join(parts)


def salary_text(salary):
    if not salary['stated']:
        return '—'

    def amount(value):
        if value is None:
            return '?'
        return f'{value / 1000:g}k' if salary['period'] in ('year', 'month') and value >= 1000 else f'{value:g}'
    low, high = amount(salary['min']), amount(salary['max'])
    span = low if salary['min'] == salary['max'] or salary['max'] is None else f'{low}–{high}'
    return f'{span} {salary["currency"]}/{salary["period"]}'


def language_leverage(p):
    """Italian among the languages a posting asks for: the edge three languages give the owner."""
    return 'it' in p['languages']


DETAIL_MIN = 6


def render(run, first_seen):
    """A short report: the shortlist and the near misses one line each, everything else as counts.

    The sweep file keeps every detail; the report only answers "anything worth my time today?"."""
    postings = run['postings']
    counts = {v: sum(1 for p in postings if p['verdict'] == v) for v in VOCAB['verdict']}
    new = sum(1 for p in postings if p['id'] not in first_seen)
    lines = [
        f'# Job sweep {run["run_id"]}',
        '',
        f'`{run["trigger"]}` · {len(postings)} evaluated, {new} new · {counts["shortlisted"]} shortlisted · '
        f'{counts["kept"]} kept · {counts["dropped"]} dropped',
        '',
        run['summary'].strip(),
        '',
    ]

    def seen(p):
        earlier = first_seen.get(p['id'])
        return 'new' if earlier is None else f'seen since {earlier[:10]}'

    def rank(p):
        est = p.get('pay_estimate') or {}
        return (-p['score']['total'], -(est.get('point') or 0), p['company'].lower())

    def pay(p):
        est = p.get('pay_estimate')
        if not est:
            return cell(salary_text(p['salary'])) if p['salary']['stated'] else None
        k = 1000 if est['period'] in ('year', 'month') else 1
        unit = 'k' if k == 1000 else ''
        figure = f'€{est["point"] / k:g}{unit}/{est["period"]}'
        if p['salary']['stated']:
            return f'{figure} (stated {cell(salary_text(p["salary"]))})'
        return f'≈{figure} (est. {est["min"] / k:g}–{est["max"] / k:g}{unit}, {est["confidence"]})'

    def line(p):
        s = p['score']
        total = f'**{s["total"]}**' if p['verdict'] == 'shortlisted' else str(s['total'])
        industry = ', '.join(p['domain']) or 'industry unknown'
        facts = [cell(p['company']), f'*{cell(industry)}*', p['contract'], cell(remote_text(p['remote']))]
        if pay(p):
            facts.append(pay(p))
        facts.append(seen(p))
        if not p['verified']:
            facts.append('*unverified*')
        return f'- {total} · {link(p)} — {" · ".join(facts)}'

    listed = sorted((p for p in postings if p['verdict'] != 'dropped'), key=rank)
    shortlist = [p for p in listed if p['verdict'] == 'shortlisted']
    lines += ['## Shortlist', '']
    if shortlist:
        for p in shortlist:
            lines.append(line(p))
            if p.get('pay_estimate') and not p['salary']['stated']:
                lines.append(f'  *Pay basis:* {cell(p["pay_estimate"]["basis"])}')
    else:
        lines.append('Nothing made the shortlist.')
    lines.append('')

    leverage = [p for p in listed if p['verdict'] != 'shortlisted' and language_leverage(p)]
    if leverage:
        lines += ['## Asks for Italian', ''] + [line(p) for p in leverage] + ['']

    near = [p for p in listed if p['verdict'] == 'kept' and not language_leverage(p)
            and p['score']['total'] >= DETAIL_MIN]
    if near:
        lines += [f'## Near misses (kept, {DETAIL_MIN}+)', ''] + [line(p) for p in near] + ['']

    tail = []
    below = sum(1 for p in listed if p['verdict'] == 'kept' and not language_leverage(p)
                and p['score']['total'] < DETAIL_MIN)
    if below:
        tail.append(f'Kept below {DETAIL_MIN}: {below}.')
    dropped = [p for p in postings if p['verdict'] == 'dropped']
    if dropped:
        reasons = [f'{r} {n}' for r in VOCAB['drop_reason']
                   if (n := sum(1 for p in dropped if p['drop_reason'] == r))]
        tail.append(f'Dropped: {", ".join(reasons)}.')
    ok = sum(1 for s in run['sources'] if s['status'] == 'ok')
    trouble = [f'{cell(s["name"])} {s["status"]}' for s in run['sources'] if s['status'] in ('blocked', 'error')]
    tail.append(f'Sources: {ok} of {len(run["sources"])} ok' + (f'; {", ".join(trouble)}.' if trouble else '.'))
    lines += [' '.join(tail), '']

    if run['open_questions']:
        lines += ['## Open questions', ''] + [f'- {q}' for q in run['open_questions']] + ['']
    return '\n'.join(lines)


def main(argv):
    new_path = Path(argv[1]).resolve() if len(argv) > 1 else None
    if new_path:
        if new_path.parent != RUNS or new_path.suffix != '.json':
            sys.exit(f'{new_path} is not a sweep file in {RUNS}')
        if committed(new_path):
            sys.exit(f'{new_path.name} is already committed; sweeps are history and are not rewritten')
        run = normalise(json.loads(new_path.read_text()))
        new_path.write_text(json.dumps(run, indent=2, ensure_ascii=False) + '\n')

    paths = sorted(RUNS.glob('*.json'))
    runs, errors = [], []
    for path in paths:
        try:
            run = json.loads(path.read_text())
        except json.JSONDecodeError as e:
            errors.append(f'{path.stem}: not valid JSON ({e})')
            continue
        errors += check(run, path.stem)
        runs.append(run)
    if errors:
        print('\n'.join(errors), file=sys.stderr)
        sys.exit(f'{len(errors)} problem(s); nothing written but the normalised sweep file')

    runs.sort(key=lambda r: r['run_id'])
    # Reports are derived, so every one is regenerated: an improved layout reaches old sweeps too, and
    # a report can never drift from its sweep. "Seen since" only looks at earlier sweeps, so this is stable.
    first_seen, fresh = {}, set()
    for run in runs:
        (RUNS / f'{run["run_id"]}.md').write_text(render(run, first_seen))
        if new_path and run['run_id'] == new_path.stem:
            new_run, fresh = run, {p['id'] for p in run['postings'] if p['id'] not in first_seen}
        for p in run['postings']:
            first_seen.setdefault(p['id'], run['run_id'])

    index = build_index(runs)
    INDEX.write_text(''.join(json.dumps(row, ensure_ascii=False) + '\n' for row in index))

    print(f'{len(runs)} sweep(s) valid; index holds {len(index)} unique posting(s).')
    if new_path:
        shortlisted = [p for p in new_run['postings'] if p['verdict'] == 'shortlisted']
        print(f'{new_run["run_id"]}: {len(new_run["postings"])} evaluated, {len(shortlisted)} shortlisted, '
              f'{len(fresh)} never seen before. Report: {new_path.with_suffix(".md").relative_to(JOBS.parent)}')


if __name__ == '__main__':
    main(sys.argv)
