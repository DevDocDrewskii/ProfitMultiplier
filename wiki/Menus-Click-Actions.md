# Menus: Click Actions

Any menu item can run a list of actions, grouped by which click triggered them.

## Format

```yaml
items:
  example:
    material: EMERALD
    slot: 13
    name: "&aClick me"
    actions:
      left:
        - "[message] &aYou left-clicked!"
        - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP 1 1"
      right:
        - "[player] warp shop"
      any:
        - "[sound] UI_BUTTON_CLICK 1 1"
```

A shorthand flat list maps to `any`:

```yaml
    actions:
      - "[close]"
```

## Click buckets

| Key | Fires on |
|-----|----------|
| `left` | Left click |
| `right` | Right click |
| `shift_left` | Shift + left click |
| `shift_right` | Shift + right click |
| `middle` | Middle click |
| `double` | Double click |
| `drop` | Drop key (Q) |
| `number_key` | Hotbar number keys |
| `any` (also `all`, `click`) | Every click, in addition to the specific bucket |

The `any` bucket always runs, plus the bucket matching the actual click. So a `left` click runs
`any` then `left`.

## Action tags

| Tag | Effect |
|-----|--------|
| `[message] <text>` | Send a chat message to the clicker. |
| `[broadcast] <text>` | Broadcast to the whole server. |
| `[console] <command>` | Run a command from console. |
| `[player] <command>` | Make the player run a command. |
| `[open] <menu>` | Open another menu file in this folder. |
| `[close]` | Close the menu. |
| `[sound] <SOUND> [volume] [pitch]` | Play a sound to the clicker. |
| `[refresh]` | Re-render the current menu in place. |
| `[next-page]` | Go to the next page (paginated menus). |
| `[previous-page]` | Go to the previous page. |
| `[page] <n>` | Jump to page `n`. |

A line with no recognised tag is treated as a `[message]`, so plain text just works.

## Tokens in actions

Action text supports the same `{tokens}` and `%papi%` placeholders as names and lore. Commands
have placeholders expanded and a leading slash stripped, so `[console] eco give %player_name% 100`
works as written.

## See also

- [Items & Icons](Menus-Items-and-Icons)
- [Pagination](Menus-Pagination)
- [Menu Tokens](Menus-Tokens)
