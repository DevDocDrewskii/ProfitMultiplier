# Menus: Tokens

Menu names, lore, and action text support three layers of substitution, applied in order:

1. **Local `{tokens}`** - computed per viewer for items bound to a `group` (and optionally a
   `tier`), or for dynamic pagination entries. These work even **without** PlaceholderAPI.
2. **`%papi%` placeholders** - expanded if PlaceholderAPI is installed. See [PlaceholderAPI](PlaceholderAPI).
3. **Colours** - `&` codes and `&#RRGGBB` hex (modern servers).

## Group / tier tokens

Available on items with `group:` set, and on dynamic entries.

| Token | Meaning |
|-------|---------|
| `{player}` | Viewer name |
| `{group}` | Group key (for example `crops`) |
| `{group_name}` | Display name, or a prettified key |
| `{sold}` / `{sold_raw}` / `{sold_short}` | Group total sold (`1,234` / `1234` / `1.2K`) |
| `{current_multiplier}` | The player's current group multiplier |
| `{max_multiplier}` | The highest multiplier the group can reach |
| `{tier_count}` | Number of tiers in the group |
| `{multiplier}` | This tier's multiplier (or current, if no `tier` is set) |
| `{threshold}` / `{threshold_raw}` / `{threshold_short}` | This tier's goal (`MAX` when maxed) |
| `{remaining}` / `{remaining_short}` | Units left to reach the goal |
| `{percent}` / `{progress_percent}` | Progress toward the goal (0-100) |
| `{progress_bar}` | A unicode progress bar (styled by the menu's `progress-bar` settings) |
| `{status}` | `status-unlocked` or `status-locked` text |
| `{unlocked}` | `true` or `false` |

## Pagination tokens

Available on every item of a paginated menu.

| Token | Meaning |
|-------|---------|
| `{page}` | Current page (1-based) |
| `{total_pages}` | Total page count |
| `{next_page}` | Next page number (clamped) |
| `{previous_page}` | Previous page number (clamped) |

Dynamic entries also get `{icon}` (the per-entry icon string) and, for `tiers:` sources,
`{tier_number}`.

## Tip

Because `{...}` tokens are computed in Java, the default menus work fully without
PlaceholderAPI. Add `%papi%` placeholders on top when you want data from other plugins.

## See also

- [PlaceholderAPI](PlaceholderAPI)
- [Items & Icons](Menus-Items-and-Icons)
- [Pagination](Menus-Pagination)
