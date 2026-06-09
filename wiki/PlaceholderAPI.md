# PlaceholderAPI

With [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) installed,
ProfitMultiplier registers the `profitmultiplier` expansion. Use these in any
PlaceholderAPI-aware plugin (scoreboards, holograms, chat, menus, and so on).

## Player totals

| Placeholder | Returns |
|-------------|---------|
| `%profitmultiplier_sold_total%` | Total units sold across all materials |
| `%profitmultiplier_bonus_total%` | Total bonus money earned from multipliers |
| `%profitmultiplier_bonus_last%` | Bonus from the last boosted sale |
| `%profitmultiplier_bonus_last_display%` | The `bonus-display` message, or empty |

## Per-material

Replace `<MAT>` with a Bukkit material name (for example `DIAMOND`).

| Placeholder | Returns |
|-------------|---------|
| `%profitmultiplier_sold_<MAT>%` | Units of that material sold |
| `%profitmultiplier_multiplier_<MAT>%` | Current multiplier for that material |
| `%profitmultiplier_next_threshold_<MAT>%` | Next threshold, or `MAX` |
| `%profitmultiplier_remaining_<MAT>%` | Units left to the next threshold |

## Per-group

Replace `<group>` with a group name (for example `crops`).

| Placeholder | Returns |
|-------------|---------|
| `%profitmultiplier_group_sold_<group>%` | Combined units sold across the group |
| `%profitmultiplier_group_multiplier_<group>%` | Current group multiplier |
| `%profitmultiplier_group_active_threshold_<group>%` | Highest threshold reached |
| `%profitmultiplier_group_next_threshold_<group>%` | Next threshold, or `MAX` |
| `%profitmultiplier_group_remaining_<group>%` | Units left to the next threshold |
| `%profitmultiplier_group_progress_<group>%` | Progress to next threshold (0-100) |
| `%profitmultiplier_group_max_multiplier_<group>%` | Highest multiplier the group can reach |
| `%profitmultiplier_group_tier_count_<group>%` | Number of tiers in the group |
| `%profitmultiplier_group_display_<group>%` | The group's display name |

## Examples

```
&7Crops sold: &f%profitmultiplier_group_sold_crops%
&7Crop multiplier: &a%profitmultiplier_group_multiplier_crops%x
&7Next tier in: &e%profitmultiplier_group_remaining_crops%
&7Diamonds: &f%profitmultiplier_sold_DIAMOND% &7(&a%profitmultiplier_multiplier_DIAMOND%x&7)
```

## Inside menus

Menus also support these `%...%` placeholders, plus their own `{tokens}` that do not need
PlaceholderAPI. See [[Menu Tokens|Menus-Tokens]].
