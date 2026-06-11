# Menus: Pagination

A menu can auto-fill a content area from a data source and page through it when there are more
entries than slots. This is exactly how the shipped `groups.yml` browses every group: it uses
one content row of 7 slots, and the plugin ships 8 default groups, so it opens on page 1 of 2.

## Format

This is the shipped `groups.yml`:

```yaml
title: "&8» &2&lMultiplier Groups"
size: 27
update: true

filler:
  enabled: true
  material: GRAY_STAINED_GLASS_PANE
  name: " "

content-slots:
  - "10-16"

content-source: groups

content:
  material: "{icon}"
  name: "&a&l{group_name}"
  lore:
    - "&7Total sold: &f{sold}"
    - "&7Current multiplier: &a{current_multiplier}x"
    - "{progress_bar} &7{progress_percent}%"
  actions:
    any:
      - "[message] &a{group_name}&7: &f{sold} &7sold."

items:
  previous:
    material: ARROW
    slot: 18
    name: "&e<- Previous Page"
    hide-on-first-page: true
    actions: { any: [ "[previous-page]" ] }
  page-info:
    material: PAPER
    slot: 22
    name: "&fPage &a{page}&7/&a{total_pages}"
  next:
    material: ARROW
    slot: 24
    name: "&eNext Page ->"
    hide-on-last-page: true
    actions: { any: [ "[next-page]" ] }
  close:
    material: BARRIER
    slot: 26
    name: "&cClose"
    actions: { any: [ "[close]" ] }
```

Add more content slots (or more `content-slots:` ranges) to show more groups per page. With a
single row of 7 slots, an 8th group spills onto page 2.

## Fields

| Field | Meaning |
|-------|---------|
| `content-slots` | The slots that hold dynamic entries. Supports ranges like `"10-16"`. |
| `content-source` | What to list. `groups` or `tiers:<group>`. |
| `content` | A template item rendered once per entry, using that entry's tokens. |

## Content sources

| Source | One entry per | Extra token |
|--------|---------------|-------------|
| `groups` | Each configured group | `{icon}` (the group's icon) |
| `tiers:crops` | Each tier of the named group | `{tier_number}`, `{icon}` (tier icon) |

The framework chunks the entries across pages based on the number of `content-slots`.

## Navigation

- Use `[next-page]`, `[previous-page]`, and `[page] <n>` actions on buttons.
- Add `hide-on-first-page: true` / `hide-on-last-page: true` so arrows disappear at the ends.
- Page tokens are available to every item on the menu: `{page}`, `{total_pages}`,
  `{next_page}`, `{previous_page}`.

## Notes

- Inventory titles are fixed when the menu opens, so do not put `{page}` in the title; show
  the page count on a page-info item instead.
- With `update: true` the current page re-renders periodically and keeps your place.

## See also

- [Menu Tokens](Menus-Tokens)
- [Click Actions](Menus-Click-Actions)
