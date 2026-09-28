# Tracker

The owner's pipeline. Written by the owner, or by `job-scout` in an interactive session when asked —
never by the scheduled routine. `id` is the posting id from `postings.jsonl`, so every row links back to
the full history of that posting.

Status: `shortlisted` → `applied` → `interview` → `offer` / `rejected` / `withdrawn`, or `passed` (decided
not to apply) and `expired` (closed before anything happened). `since` is the date the status last
changed.

| id | company | role | contract | status | since | link | notes |
| --- | --- | --- | --- | --- | --- | --- | --- |
