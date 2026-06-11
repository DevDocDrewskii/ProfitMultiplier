# Stacking Multipliers

When a material belongs to **both** an `items` ladder and a [group](Configuration-Groups),
the group's `stack-mode` decides how the two multipliers combine. For a material that is only
in a group (the common case), all three modes behave identically.

## Modes

| `stack-mode` | Behaviour |
|--------------|-----------|
| `group` (default) | The group multiplier applies; the material's own multiplier is ignored. |
| `item` | The material's own multiplier applies; the group multiplier is ignored. |
| `stack` | Both apply, multiplied together. |

Set it per group:

```yaml
groups:
  ores:
    stack-mode: stack
    materials: [DIAMOND, GOLD_INGOT, IRON_INGOT, EMERALD]
    tiers:
      - { threshold: 5000,   multiplier: 1.1 }
      - { threshold: 50000,  multiplier: 1.2 }
      - { threshold: 250000, multiplier: 1.3 }
```

## How stacking math works

With `stack`, the effective multiplier for a unit is the material multiplier times the group
multiplier. For example, if DIAMOND's own ladder gives 1.5x and the ores group gives 1.2x at
the player's current totals, a diamond sells at `1.5 * 1.2 = 1.8x`.

Both counters advance together during a sale, and the boost is recomputed at every threshold
either counter crosses, so a large sale that straddles a boundary is still priced correctly
segment by segment.

## Milestone messages

- In `group` or `stack` mode, crossing a group threshold sends the
  `group-threshold-reached` message.
- In `item` or `stack` mode, crossing the material's own threshold sends the
  `threshold-reached` message.

So with `stack`, a single sale can announce both a group milestone and an item milestone.

## Picking a default

`group` is the default because it preserves the simplest behaviour: one ladder per group with
no surprises. Use `stack` when you want a group to be a global bonus layered on top of
per-item ladders, and `item` when the group is only there for display or placeholders.

## See also

- [Groups](Configuration-Groups)
- [Items](Configuration-Items)
