# Commands & Permissions

The base command is `/profitmultiplier`, with aliases `/pm` and `/sellmulti`.

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/sellmulti` | Open the sell-multiplier menu | `profitmultiplier.gui` |
| `/pm gui [menu] [player]` | Open a menu for yourself, or for another player | `profitmultiplier.gui` (`.admin` to open for others) |
| `/pm stats [player]` | View sell progression | `profitmultiplier.stats` |
| `/pm reload` | Reload config, lang, currencies and menus | `profitmultiplier.admin` |
| `/pm reset <player>` | Reset one player's sold totals | `profitmultiplier.admin` |
| `/pm resetall` | Reset every player's sold totals | `profitmultiplier.admin` |
| `/pm help` | Show the help list | none |

### Notes

- `/sellmulti` with no arguments opens the default menu (`menus/sellmulti.yml`).
- `/pm gui <menu>` opens any other menu file by name (without the `.yml`). Tab-complete lists
  the available menus.
- `/pm gui <menu> <player>` opens a menu for another online player (requires admin).

## Permissions

| Permission | Default | Grants |
|------------|---------|--------|
| `profitmultiplier.gui` | everyone | Open menus |
| `profitmultiplier.stats` | everyone | View your own stats |
| `profitmultiplier.admin` | op | Reload, reset, resetall, and open menus for other players |

Set defaults or grant nodes through your permissions plugin (LuckPerms, etc.) as usual.

## Examples

```
/sellmulti
/pm gui groups
/pm stats Notch
/pm reset Notch
/pm reload
```
