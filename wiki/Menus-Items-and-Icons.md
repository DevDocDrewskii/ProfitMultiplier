# Menus: Items & Icons

Each entry under `items:` in a menu file is one clickable icon.

## Full example

```yaml
items:
  tier-1:
    material: GRASS_BLOCK
    slot: 11
    group: crops
    tier: 10000
    name: "&a&l1.1x Sell Multiplier"
    glow: false
    lore:
      - "&7Sell &f{threshold} &7crops to unlock."
      - "&7Sold: &f{sold}&7/&f{threshold}"
      - "{progress_bar} &7{progress_percent}%"
      - "&7Status: {status}"
    actions:
      left:
        - "[message] &7Keep selling crops!"
        - "[sound] UI_BUTTON_CLICK 1 1"
```

## Fields

| Field | Meaning |
|-------|---------|
| `material` / `item` / `icon` | The icon. Any form below. |
| `slot` | A single slot index (0-based). |
| `slots` | A list of slots; supports ranges like `"10-16"`. |
| `amount` | Stack size (1-64). |
| `name` | Display name. Supports `&` codes, hex, `%papi%`, and `{tokens}`. |
| `lore` | List of lore lines (same formatting). |
| `glow` | `true` adds an enchant glow with the enchant hidden. |
| `custom-model-data` / `model-data` | Custom model data integer (1.14+). |
| `item-flags` | A list of Bukkit `ItemFlag` names (for example `HIDE_ATTRIBUTES`). |
| `view-permission` | If set, the item only shows to players with this permission. |
| `group` | Bind the item to a [group](Configuration-Groups) for live tokens. |
| `tier` | A specific threshold within the bound group, for tier display. |
| `hide-on-first-page` / `hide-on-last-page` | Hide on those pages (great for nav arrows). |
| `actions` | Per-click actions. See [Click Actions](Menus-Click-Actions). |

## Icon forms

| Form | Result |
|------|--------|
| `WHEAT` | Vanilla material (legacy 1.8 names auto-mapped). |
| `material:STONE` | Explicit vanilla material. |
| `nexo:my_id` | Nexo custom item. |
| `oraxen:my_id` | Oraxen custom item. |
| `itemsadder:ns:id` | ItemsAdder custom item. |
| `hdb:1234` | HeadDatabase head (also `headdatabase:1234`). |
| `player:Notch` | Player-skinned head (also `playerhead:Notch`). |
| `basehead:eyJ0...` | Base64-textured head (also `head:` or `base64:`). |

Custom-item plugins are queried only if installed. An unknown or unavailable icon falls back
to a barrier so the mistake is visible in-game rather than silent.

## Group and tier binding

When an item sets `group:` (and optionally `tier:`), ProfitMultiplier computes live, per-viewer
tokens such as `{sold}`, `{multiplier}`, `{threshold}` and `{progress_bar}`. These work even
without PlaceholderAPI. See [Menu Tokens](Menus-Tokens) for the full list.

## See also

- [Menu Tokens](Menus-Tokens)
- [Click Actions](Menus-Click-Actions)
