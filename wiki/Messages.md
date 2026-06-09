# Messages (lang.yml)

All player-facing text lives in `plugins/ProfitMultiplier/lang.yml`. Every message has a
master toggle and a text body. A message is only sent when it is enabled **and** the text is
not empty, so you can silence any message two ways.

## Format

```yaml
prefix: "&8[&6ProfitMultiplier&8]&r "

messages:
  multiplier-applied:
    enabled: true
    message: "{prefix}&fSold &e{amount}x {item} &ffor &a{final} &7(+&a{bonus}&7, &b{multiplier}x&7)"
```

- Colour codes use `&`. `&#RRGGBB` hex works on modern servers.
- `{prefix}` inserts the prefix defined at the top.
- To silence a message: set `enabled: false`, or clear its `message` value.

## Messages and their tokens

| Key | Tokens |
|-----|--------|
| `multiplier-applied` | `{amount} {item} {base} {final} {bonus} {currency} {multiplier} {total}` |
| `threshold-reached` | `{item} {total} {multiplier} {threshold}` |
| `group-threshold-reached` | `{group} {total} {multiplier} {threshold}` |
| `bonus-display` | `{bonus}` |
| `stats-header` | `{player}` |
| `stats-line` | `{item} {sold} {multiplier}` |
| `stats-none` | `{player}` |
| `reset-player` / `reset-player-none` | `{player}` |
| `reset-all` | `{count}` |
| `menu-unknown` | `{menu}` |
| `menu-opened-other` | `{player} {menu}` |
| `player-not-online` | `{player}` |
| `no-permission`, `config-reloaded`, `usage-reset`, `console-needs-player`, `unknown-subcommand` | none |

## Currency formatting

`{base}`, `{final}` and `{bonus}` are already formatted with the relevant
[[currency|Currencies]] (symbol included). Do not add your own `$` around them. Use
`{currency}` if you want the bare symbol.

## See also

- [[Custom Currencies|Currencies]]
- [[PlaceholderAPI|PlaceholderAPI]]
