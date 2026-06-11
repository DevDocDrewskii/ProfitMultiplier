# Configuration

All settings live in `plugins/ProfitMultiplier/config.yml`. After editing, run `/pm reload`.

## Sections at a glance

| Section | Purpose | Page |
|---------|---------|------|
| `hooks` | Enable/disable each shop integration | [Shops](Shops) |
| `currency` | Display symbols and formats; named currencies | [Currencies](Currencies) |
| `items` | Per-material multiplier ladders | [Items](Configuration-Items) |
| `groups` | Shared ladders across many materials | [Groups](Configuration-Groups) |
| `default` | Fallback ladder for unlisted items | this page |
| `threshold-scaling` | Permission-based threshold discounts for donor ranks | [Milestones & Discord](Milestones) |
| `milestones` | Tier-up console commands and Discord webhooks | [Milestones & Discord](Milestones) |
| `auto-reset` | Periodic progression wipe | [Auto-Reset](Configuration-Auto-Reset) |
| `debug` | Verbose console logging | this page |

## Default ladder

Applies to every material that is **not** matched by an `items` ladder or a `group`.

```yaml
default:
  # false = unlisted items never get a multiplier.
  enabled: true
  # Cumulative amount of an unlisted item a player must sell to unlock the boost.
  threshold: 1000
  # Multiplier applied once the threshold is reached.
  multiplier: 1.1
  # Items that never receive the default multiplier, even when enabled.
  blacklist:
    - BEDROCK
    - BARRIER
    - COMMAND_BLOCK
    - STRUCTURE_BLOCK
```

To disable the fallback entirely, set `enabled: false`.

## Debug

```yaml
debug: false
```

Set to `true` to log every multiplier application and hook detail to the console. Useful when
setting up a new shop or diagnosing why a sale was or was not boosted.

## Material names

Material names follow the Bukkit `Material` enum (`DIAMOND`, `GOLD_INGOT`, `WHEAT`, ...).
Legacy 1.8 names and modern 1.13+ names are both accepted and mapped automatically, so a
config written for one era works on the other.

## Reloading

`/pm reload` reloads `config.yml`, `lang.yml`, the currency definitions, and all menu files.
It does not re-register shop hooks (those are bound once at startup), so if you change a
`hooks` toggle, restart the server.

## Updating the plugin (auto-merge)

When you drop in a newer jar, ProfitMultiplier keeps your configs current automatically - both
on startup and on `/pm reload`:

- **New config keys** (for example new default groups or currencies) are merged into your
  existing `config.yml`. Your existing values are never changed.
- **New messages** are merged into your existing `lang.yml`.
- **New menu files** bundled with the plugin are written to `menus/` if they are missing.
  Existing menu files are never overwritten.

The console logs `Updated config.yml with new default keys` when a merge happens.

Caveat: the merge is additive and keyed on "is this default missing". If you **delete** a
default entry (say you remove the `building` group), it is re-added on the next load. To
suppress a group you do not want, empty its `materials` list instead of deleting the block.
