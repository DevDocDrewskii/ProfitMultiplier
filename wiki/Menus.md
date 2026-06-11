# Menus (GUI)

Menus are defined by `.yml` files in `plugins/ProfitMultiplier/menus/`. Two ship by default:

- `sellmulti.yml` - opened by `/sellmulti`. Includes an "All Multiplier Groups" button that
  opens `groups.yml` via an `[open] groups` action.
- `groups.yml` - a paginated browser of every group, opened by `/pm gui groups`.

Drop in more `.yml` files to create more menus, then open them with `/pm gui <filename>`
(without the `.yml`). New bundled menu files are written to `menus/` automatically on update;
your existing files are never overwritten.

## Top-level options

```yaml
title: "&8» &2&lSell Multipliers"
rows: 3                 # 1-6 rows (or use `size:` for an exact slot count)
update: true            # live-refresh while open, so progress stays current
open-permission: ""     # optional permission required to open this menu

status-unlocked: "&aUNLOCKED"
status-locked: "&cLOCKED"
progress-bar:
  length: 20
  symbol: "|"
  complete: "&a"
  incomplete: "&7"

filler:
  enabled: true
  material: GRAY_STAINED_GLASS_PANE
  name: " "
```

| Option | Meaning |
|--------|---------|
| `title` | Top bar text. Supports `&` codes, `&#RRGGBB` hex, and `%papi%`. Fixed when the menu opens. |
| `rows` / `size` | Inventory size. `rows` is 1-6; `size` is an exact slot count (rounded to a multiple of 9, max 54). |
| `update` | If `true`, the menu re-renders periodically so progress bars and placeholders stay live. |
| `open-permission` | If set, players need this permission to open the menu. |
| `status-unlocked` / `status-locked` | Text for the `{status}` token on tier items. |
| `progress-bar` | Appearance of the `{progress_bar}` token. |
| `filler` | Item used to fill empty slots. Set `enabled: false` to leave gaps. |

## Items

Each entry under `items:` is one icon. See [Items & Icons](Menus-Items-and-Icons) for the
full field list, and [Menu Tokens](Menus-Tokens) for the live placeholders you can use in
names and lore.

## Click actions

Every item can run actions per click type (left, right, shift, and more). See
[Click Actions](Menus-Click-Actions).

## Pagination

A menu can auto-fill a content area from a data source (all groups, or the tiers of one group)
and page through it. See [Pagination](Menus-Pagination).

## See also

- [Items & Icons](Menus-Items-and-Icons)
- [Click Actions](Menus-Click-Actions)
- [Pagination](Menus-Pagination)
- [Menu Tokens](Menus-Tokens)
