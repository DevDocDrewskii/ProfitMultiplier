# Configuration: Groups

A group is a named bucket of materials that share **one** cumulative counter. Selling any
member advances the same total, and the unlocked multiplier applies to every member. This is
what powers the "crops" example: wheat, carrots, kelp and the rest all push the same crop
progress.

## Format

```yaml
groups:
  crops:
    icon: GRASS_BLOCK
    display-name: "Crops"
    stack-mode: group
    # currency: gems
    materials:
      - WHEAT
      - CARROT
      - POTATO
      - KELP
      - SUGAR_CANE
    tiers:
      - threshold: 10000
        multiplier: 1.1
        icon: GRASS_BLOCK
      - threshold: 100000
        multiplier: 1.2
        icon: WHEAT
      - threshold: 1000000
        multiplier: 1.3
        icon: KELP
```

## Fields

| Field | Required | Meaning |
|-------|----------|---------|
| `materials` | yes | Materials whose sales feed the shared counter. |
| `tiers` | yes | `{ threshold, multiplier, icon? }` ladder (icon is optional, per tier). |
| `icon` | no | Menu icon for the group. Any [[icon form|Menus-Items-and-Icons]] (MATERIAL, `nexo:id`, `hdb:1234`, ...). |
| `display-name` | no | Pretty name shown by `{group_name}` and `%..._group_display_<g>%`. |
| `stack-mode` | no | How the group combines with a material's own multiplier: `group` (default), `item`, or `stack`. See [[Stacking Multipliers|Stacking-Multipliers]]. |
| `currency` | no | A named currency from the [[currency section|Currencies]], used to format this group's payout messages. |

## Default groups

The plugin ships with eight starter groups so you can see the system working immediately. Trim
or edit any you do not want.

| Group | Theme | Notable setting |
|-------|-------|-----------------|
| `crops` | Farming | `stack-mode: group` |
| `ores` | Mined ores | `stack-mode: stack` (stacks on the DIAMOND/GOLD/IRON item ladders) |
| `resources` | Logs and wood | - |
| `excavation` | Dirt, sand, gravel, stone | - |
| `fishing` | Fish and sea loot | `currency: gems` |
| `nether` | Nether materials | - |
| `mob_drops` | Combat drops | `currency: tokens` |
| `building` | Building blocks | - |

All eight appear automatically in the paginated [[group browser|Menus-Pagination]]
(`/pm gui groups`).

## Make your own

Add as many groups as you like - just copy a block and rename it. Every group you define is
instantly available in three places, with no extra wiring:

- Its own [[PlaceholderAPI placeholders|PlaceholderAPI]] (swap `crops` for the group name).
- A slot in any paginated `content-source: groups` menu (see [[Pagination|Menus-Pagination]]).
- A `group:`-bound menu item (see [[Menu Tokens|Menus-Tokens]]).

When you update the plugin, new default groups (and any other new config keys) are merged into
your existing `config.yml` automatically. See [[Updating|Configuration]].

## Precedence

For a material only in a group, the group multiplier is the one that applies. When a material
is also in an `items` ladder, the `stack-mode` decides. Default ladder never applies to a
material that belongs to a group.

## See also

- [[Stacking Multipliers|Stacking-Multipliers]]
- [[Custom Currencies|Currencies]]
- [[Examples & Recipes|Examples]]
