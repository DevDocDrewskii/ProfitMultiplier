# Custom Currencies

ProfitMultiplier never moves money itself. It scales the price the shop transaction already
uses, so the multiplier is **currency-agnostic** and works with whatever your shop pays in
(Vault, PlayerPoints, tokens, gems, and so on). This section only controls how amounts are
**displayed** in messages and placeholders.

## Format

```yaml
currency:
  symbol: "$"
  position: PREFIX        # PREFIX -> "$1,234"   |   SUFFIX -> "1,234 gems"
  format: "#,##0.##"
  custom:
    gems:
      symbol: " gems"
      position: SUFFIX
      format: "#,##0"
    tokens:
      symbol: "tokens "
      position: PREFIX
      format: "#,##0"
```

## Fields

| Field | Meaning |
|-------|---------|
| `symbol` | The currency symbol or label. |
| `position` | `PREFIX` (before the number) or `SUFFIX` (after). |
| `format` | A Java `DecimalFormat` pattern, for example `#,##0.##`. |
| `custom` | A map of named currencies, each with the same three fields. |

The top-level `symbol`/`position`/`format` define the **default** currency. Entries under
`custom` define additional named currencies.

## Assigning a currency to a group

Set `currency: <name>` on a [[group|Configuration-Groups]] so that group's payout messages use
that currency's formatting:

```yaml
groups:
  mob_drops:
    currency: tokens
    materials: [ROTTEN_FLESH, BONE, STRING, GUNPOWDER]
    tiers:
      - { threshold: 5000, multiplier: 1.1 }
```

A sale in that group then renders as, for example, `tokens 1,234` in the
`multiplier-applied` message.

## Notes

- The numeric value passed to the currency formatter is the shop transaction's own amount, so
  the symbol is purely cosmetic - it does not change which economy is paid.
- `{base}`, `{final}` and `{bonus}` in the `multiplier-applied` message are already
  currency-formatted (symbol included), so do not add your own symbol in `lang.yml`. The bare
  symbol is available as `{currency}` if you want it.

## See also

- [[Messages (lang.yml)|Messages]]
- [[Groups|Configuration-Groups]]
