# Milestones, Threshold Scaling & Discord

Three related systems that fire when a player crosses a multiplier threshold:

- **Milestone commands** - run console commands on tier-up (keys, titles, broadcasts, ...).
- **Threshold scaling** - donor ranks unlock tiers earlier via permission nodes.
- **Discord webhooks** - announce milestones to a channel, as a classic embed or a
  Components V2 message.

All of it lives in `config.yml` and reloads with `/pm reload`.

## Placeholders

Every milestone command and every Discord text field accepts:

| Placeholder | Value |
|-------------|-------|
| `{player}` | Player name |
| `{uuid}` | Player UUID (handy for avatar services) |
| `{name}` | The ladder's display name (group display name, or item name) |
| `{item}` | Item name, e.g. `Gold Ingot` |
| `{item_raw}` | Bukkit material name, e.g. `GOLD_INGOT` |
| `{group}` | Group display name (empty on item ladders) |
| `{ladder}` | `item` or `group` |
| `{tier}` / `{tiers}` | Tier number reached / total tiers in the ladder |
| `{threshold}` | The threshold that was crossed (after scaling) |
| `{threshold_formatted}` | Same, with thousands separators |
| `{total}` / `{total_formatted}` | The player's new sold total on that ladder |
| `{multiplier}` | The multiplier unlocked, e.g. `1.25` |

## Milestone commands

Commands are run by the **console**, on the main thread, at the moment the tier is crossed.
A bulk sale that jumps several tiers at once fires each crossed tier in order.

### Global commands

```yaml
milestones:
  commands:
    on-tier:
      - "crates give {player} key vote 1"
      - "broadcast &6{player} &ereached &b{multiplier}x &eon &b{name}&e!"
    on-max-tier:
      - "lp user {player} permission set rank.grinder true"
      - "titles give {player} grinder"
```

- `on-tier` runs for **every** tier a player unlocks, on any ladder.
- `on-max-tier` additionally runs when the unlocked tier is the **final** tier of its ladder.
- Empty lists disable them. No extra toggle needed.

### Per-group and per-material commands

Any group or `items` ladder can carry its own `milestones` block - it fires for that ladder
only, without repeating commands on every tier:

```yaml
items:
  DIAMOND:
    tiers:
      - threshold: 20
        multiplier: 1.1
      - threshold: 100
        multiplier: 1.5
    milestones:
      on-tier:
        - "say {player} stepped up their Diamond game ({tier}/{tiers})"
      on-max-tier:
        - "crates give {player} key diamond 1"

groups:
  crops:
    tiers:
      - threshold: 10000
        multiplier: 1.1
      - threshold: 1000000
        multiplier: 1.3
    milestones:
      on-max-tier:
        - "broadcast &a{player} mastered Crops!"
        - "lp user {player} permission set titles.farmer true"
```

### Per-tier commands

For tier-specific rewards, any single tier can also carry a `commands` list:

```yaml
groups:
  crops:
    tiers:
      - threshold: 1000000
        multiplier: 1.3
        commands:
          - "broadcast &a{player} sold a MILLION crops!"
```

### Order of execution

All matching lists run, in this order: the tier's own `commands`, the ladder's
`milestones.on-tier` (and `on-max-tier` if it is the final tier), then the global
`milestones.commands` lists.

The single-threshold `default` ladder (unlisted items) does **not** fire milestones unless
you set `milestones.include-default-ladder: true` - it counts as a 1-tier ladder, so every
crossing would be a "max tier", which gets noisy fast.

## Threshold scaling (donor ranks)

Let ranked players unlock tiers with fewer lifetime sales. `scale` multiplies every
threshold: `0.8` means **20% less** is needed. When a player matches several ranks, the
**lowest scale wins**. Values above `1.0` are allowed (harder, not easier).

```yaml
threshold-scaling:
  enabled: true
  ranks:
    vip:
      permission: profitmultiplier.scale.vip
      scale: 0.8
    mvp:
      permission: profitmultiplier.scale.mvp
      scale: 0.65
```

Grant the permission node through your permissions plugin (LuckPerms etc.). The node names
are free-form - define whatever fits your rank layout.

Scaling applies everywhere consistently: the sale boost itself, threshold-reached messages,
milestone commands, Discord announcements, `/pm stats`, menu progress bars, and
PlaceholderAPI values (for online players). The [[Developer API|Developer-API]] reports
**unscaled** base values.

## Discord webhooks

```yaml
milestones:
  discord:
    enabled: true
    webhook-url: "https://discord.com/api/webhooks/..."
    username: "ProfitMultiplier"
    avatar-url: ""
    announce: max-tier        # max-tier or every-tier
    style: embed              # embed or components-v2
```

| Key | Meaning |
|-----|---------|
| `webhook-url` | The webhook URL from your Discord channel settings. |
| `username` / `avatar-url` | Optional overrides for the webhook's name and avatar. |
| `announce` | `max-tier` (default) posts only final-tier milestones; `every-tier` posts all. |
| `style` | `embed` for a classic rich embed, `components-v2` for the new component layout. |

Webhook requests are sent **asynchronously** - they never block the server thread.

### Embed style

```yaml
    embed:
      title: "🏆 Milestone reached!"
      description: "**{player}** unlocked **{multiplier}x** on **{name}**!"
      color: "#FFD700"
      thumbnail: "https://mc-heads.net/avatar/{uuid}"
      footer: "ProfitMultiplier"
      timestamp: true
      fields:
        - name: "Tier"
          value: "{tier}/{tiers}"
          inline: true
        - name: "Total sold"
          value: "{total_formatted}"
          inline: true
```

Every key is optional. `color` accepts `#RRGGBB`, `0xRRGGBB`, or a decimal integer.
Discord markdown works in `description` and field values.

### Components V2 style

Uses Discord's [Components V2](https://docs.discord.com/developers/components/overview)
message layout (sent with the `IS_COMPONENTS_V2` flag and `with_components=true`). The
message is one **container** (with optional `accent-color`) holding your components in
order:

```yaml
    style: components-v2
    components-v2:
      accent-color: "#FFD700"
      components:
        - type: section
          text: "## 🏆 Milestone reached\n**{player}** unlocked **{multiplier}x** on **{name}**"
          thumbnail: "https://mc-heads.net/avatar/{uuid}"
        - type: separator
        - type: text
          text: "-# Tier {tier}/{tiers} • Total sold: {total_formatted}"
        - type: image
          url: "https://example.com/banner.png"
```

| Component | Keys | Renders as |
|-----------|------|------------|
| `text` | `text` | A text display (Discord markdown, `\n` for line breaks). |
| `section` | `text`, `thumbnail` | Text with a thumbnail accessory on the right. Without `thumbnail` it falls back to plain text. |
| `separator` | `divider` (default `true`), `spacing` (`small`/`large`) | A horizontal divider / spacing gap. |
| `image` | `url` | A full-width image (media gallery). |

Components V2 is supported by regular channel webhooks - no bot application needed.
Interactive components (buttons, selects) are not available to plain webhooks, so they are
intentionally not offered here.

## Troubleshooting

- Nothing posts to Discord: check the console for `Discord webhook returned HTTP ...`
  warnings - the body of the warning includes Discord's error message.
- Commands don't run: they execute as console - test them in the console first, and
  remember placeholders like `{player}` are replaced before dispatch.
- A player tier-ups twice after a reset: milestones fire on *crossing* a threshold, so
  after `/pm reset` the progression (and its milestones) starts over by design.
