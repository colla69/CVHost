# minion-army config for CVHost. Only the settings that differ from project.example/config.sh are here.

# Build, dependency and deploy files, the .gitignore files that keep secrets out of this public repo,
# and the CLAUDE.md files the agents read as their rules.
OWNER_ONLY_PATHS='^(\.gitignore|.*/\.gitignore|(.*/)?CLAUDE\.md|DEPLOYMENT\.md|infra/.*|scripts/.*|frontend/(package(-lock)?\.json|vue\.config\.js|babel\.config\.js|\.eslintrc\.js|playwright\.config\.js|pom\.xml))$'

# The checks of CLAUDE.md and CI: lint, build, the Playwright tests, and the CDK type-check.
WORKER_TOOLS=(
  'Bash(npm --prefix frontend ci)'
  'Bash(npm --prefix frontend run lint)'
  'Bash(npm --prefix frontend run lint -- --no-fix)'
  'Bash(npm --prefix frontend run build)'
  'Bash(npm --prefix frontend test)'
  'Bash(npm --prefix frontend test -- *)'
  'Bash(npm --prefix infra ci)'
  'Bash(npx --prefix infra tsc --noEmit -p infra)'
)
