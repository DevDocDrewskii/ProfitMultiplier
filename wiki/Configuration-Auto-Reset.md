# Auto-Reset

Auto-reset wipes **every** player's sold totals on a schedule, sending all progression back to
zero. Useful for seasons, map resets, or recurring competitions.

## Format

```yaml
auto-reset:
  enabled: false
  # HOURLY, DAILY, WEEKLY, MONTHLY, or a plain number of hours (for example 72).
  interval: WEEKLY
```

## Fields

| Field | Meaning |
|-------|---------|
| `enabled` | Master switch. `false` disables auto-reset entirely. |
| `interval` | `HOURLY`, `DAILY`, `WEEKLY`, `MONTHLY`, or a number of hours such as `72`. |

## How it works

- The plugin records the time of the last reset and checks periodically (and on startup).
- When `now - lastReset` exceeds the interval, every player's totals are cleared and the
  `ServerDataResetEvent` fires with cause `AUTOMATIC`.
- The schedule is wall-clock based on the stored last-reset time, not tied to server restarts.

## Manual resets

- `/pm reset <player>` - clears one player (cause `COMMAND`).
- `/pm resetall` - clears everyone (cause `COMMAND`).
- The [Developer API](Developer-API) can reset with cause `API`.

## See also

- [Commands & Permissions](Commands-and-Permissions)
- [Developer API](Developer-API)
