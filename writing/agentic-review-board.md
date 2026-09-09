# I gave my trading bot a review board

*Andrea Colarieti Tosti · September 2026*

I built a crypto trading bot to learn agentic architecture. The bot works, which still surprises me.
But the bot is not the interesting part. The interesting part is that it has eleven reviewers, none of
them human, and their rules were written by my own outages.

This is what I'd tell someone who wants to use coding agents on something that matters, rather than on
a to-do app.

---

## The setup

[PlayCryptoWithAI](https://github.com/colla69/PlayCryptoWithAI) trades 37 Binance spot pairs on 12h
candles, unattended, in Docker. The repository carries a `.claude/` directory alongside the code:

- **11 agents** — `risk-reviewer`, `security-reviewer`, `backtest-reviewer`, `pre-commit-reviewer`,
  `project-reviewer` and a general `reviewer`, plus `strategy-designer`, `developer`, `tester`,
  `analyst` and `docs-updater`.
- **5 skills** — clean code, risk management, security, testing, trading strategy.
- **4 rule files** — the project rules alone run to about 18,000 characters.
- **3 commands** — debug an issue, review changes, write a commit message.

That sounds like a lot of ceremony for a side project. It is roughly the amount of ceremony required to
stop me losing money.

---

## Reviewers are read-only, and that is not a detail

Every reviewing agent has `tools: Read, Grep, Glob`. No `Edit`. No `Write`. No `Bash`.

The agents that *build* things — `strategy-designer`, `developer` — can write. The agents that *judge*
things cannot. This costs nothing and removes an entire category of failure: a reviewer that can edit
is a reviewer that can quietly make a problem go away instead of reporting it, and you will not notice,
because the diff it produces looks like progress.

It is the same instinct as not letting your test suite modify the code it is testing. Obvious once
stated, easy to skip when you're moving fast and it's your own project and who's going to know.

The other half of the same idea: the reviewers that guard capital run on the strongest model available.
`docs-updater` does not need to. Spend the capability where the mistakes are expensive.

---

## The rules came from incidents, not from best practices

This is the part I'd actually recommend copying.

My `risk-reviewer` does not contain generic advice about risk management. It contains this, more or
less verbatim:

> Block any change that lets a class of open positions go unmarked: the freeze is silent because the
> dashboard overrides prices from its own map (frozen core-equity incident, 2026-08-04 → 09, when only
> `checkRisk` wrote the mark and the risk loop skipped core legs).

> Block any restore change that attributes free balance without that reservation (phantom-position
> incident, 2026-08-03: equity inflated ~25%).

Both of those are real. Both cost me either money or an accurate view of my money, which in a trading
system are close to the same thing. Both were subtle: the first was *silent*, because the dashboard
was helpfully rendering prices from a different source, so everything looked fine while the equity
figure driving my position sizing had quietly frozen.

The patch fixes the instance. The rule fixes the class. Writing the mechanism into a reviewer means
that in four months, when I have completely forgotten why `markPrice()` must stay valuation-only,
something will still stop me breaking it.

Traditional teams do this with post-mortems and a wiki nobody reads. The difference here is that the
document is *executable* — it gets consulted on every change, automatically, by something that has
actually read the diff.

If you take one thing from this: **after an incident, don't just fix it and don't just write it down.
Write it down somewhere that gets read at the moment it matters.**

---

## The agents made it easier to fool myself, so I had to build against that

Agentic tooling is very good at producing things that look finished. In trading that is genuinely
dangerous, because the failure mode of a backtest is not "it crashes" — it's "it prints a beautiful
number that will never happen again."

Two guards, both encoded as rules rather than intentions:

**No lookahead.** The `strategy-designer` agent carries a hard gate: exclude the forming candle, use
`candles.slice(0, -1)`. Every strategy must return a bounded confidence and must always return
something. Lookahead bias is the single most common way people produce a backtest that makes them rich
on paper and poor in reality, and it creeps in through a single off-by-one on an array index.

**Reproducibility over headline numbers.** At one point I had better figures than the ones published
now. I deleted them. They came from a run I could not reproduce exactly, which means they were not a
result, they were an anecdote. The README now shows a lower, fully reproducible baseline from a
committed runner:

```
last 90d (most OOS): +15.3% · Sharpe 3.04 · Max DD −4.4% · WR 53%
full history (386d): +24.6% · Sharpe 1.32 · Max DD −3.7% · WR 40%
```

Ninety days is ninety days — I am not claiming an edge that will survive a regime change. But I know
where the numbers came from and I can produce them again, and for something that runs unattended with
real money that matters more than the number being impressive.

---

## What actually went wrong

It would be dishonest to write this as though the setup was clean from the start.

For a while I force-added the candle caches past my own `.gitignore`, because it was convenient. Every
time a repair rewrote them, that was hundreds of megabytes of new blobs and a permanently dirty working
tree in between. The repository is still carrying the consequences. The gitignore now has an
angry comment in it addressed to my future self.

The rules also drift. A rule that describes an old architecture is worse than no rule, because it is
confidently wrong and something is now enforcing it. Rule files need the same maintenance as code, and
I have not solved that — I have only noticed it.

And none of this replaces understanding the system. The reviewers catch the classes of mistake I have
already made. They are silent on the ones I have not made yet.

---

## What generalises

Strip out the trading and this is what's left, and I think it holds for any codebase where mistakes
cost something:

1. **Separate the agents that build from the agents that judge**, and give the judges no write access.
2. **Route model capability to where errors are expensive**, not uniformly.
3. **Turn incidents into rules**, with the mechanism written down, not the symptom.
4. **Encode the traps specific to your domain** — the off-by-one that produces lookahead, the field
   that must never go unwritten — because generic "review my code" prompts will not find them.
5. **Prefer reproducible over impressive**, and be suspicious of your own good results.

None of that is really about AI. It's ordinary engineering discipline — least privilege, post-incident
learning, reproducible builds — applied to a new kind of collaborator. The tooling just made it cheap
enough to actually do on a project with a team of one.

---

*The code is at [github.com/colla69/PlayCryptoWithAI](https://github.com/colla69/PlayCryptoWithAI),
`.claude/` included. Fair warning: the repository is large, for reasons described above.*
