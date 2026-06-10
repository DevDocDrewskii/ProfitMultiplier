# Configuration: Items

A per-material ladder tracks one material on its own counter. The highest tier whose
threshold the player has reached is the one applied.

## Format

```yaml
items:
  DIAMOND:
    tiers:
      - threshold: 20
        multiplier: 1.1
      - threshold: 50
        multiplier: 1.25
      - threshold: 100
        multiplier: 1.5
  GOLD_INGOT:
    tiers:
      - threshold: 32
        multiplier: 1.15
      - threshold: 64
        multiplier: 1.3
  IRON_INGOT:
    tiers:
      - threshold: 64
        multiplier: 1.1
      - threshold: 128
        multiplier: 1.2
```

## Fields

| Field | Meaning |
|-------|---------|
| `<MATERIAL>` | A Bukkit material name (key). |
| `tiers` | An ordered list of `{ threshold, multiplier, commands? }`. |
| `threshold` | Cumulative units of this material the player must sell to unlock the tier. |
| `multiplier` | The sell multiplier once the threshold is reached (1.0 = no change). |
| `commands` | Optional console commands run when the tier is unlocked. See [[Milestones & Discord|Milestones]]. |
| `milestones` | Optional `on-tier` / `on-max-tier` command lists for this material's ladder. See [[Milestones & Discord|Milestones]]. |

Tiers do not need to be sorted in the file; they are sorted by threshold on load.

## How it reads

If a player has sold 60 diamonds with the ladder above, they are on the `50 -> 1.25x` tier.
At 100 total they move to `1.5x`. The multiplier never drops unless the totals are reset.

## Interaction with groups

If a material appears in both an `items` ladder and a [[group|Configuration-Groups]], the
group's [[stack mode|Stacking-Multipliers]] decides which applies, or whether they multiply
together. With the default `stack-mode: group`, the group wins for its members.

## See also

- [[Groups|Configuration-Groups]]
- [[Stacking Multipliers|Stacking-Multipliers]]
- [[How It Works|How-It-Works]]
