# How It Works

ProfitMultiplier tracks how much of each item every player has sold over time. As a player's
running total crosses a threshold, their sell multiplier for that item (or group) goes up and
stays up for every future sale.

## Per-unit boosting

The boost is applied **per unit**. When a single sale crosses a threshold, only the units at
or after the threshold are boosted; the rest sell at the old rate. This keeps progression
smooth and exploit-free, even on huge stacked sales.

Example: a player has sold 19 diamonds and the 20th unlocks 1.1x. If they sell 5 at once
(units 20-24), unit 20 onward is boosted and the math is computed segment by segment.

## The three ladder types

When an item is sold, ProfitMultiplier looks for a multiplier in this order:

1. **Items** - a per-material ladder defined under `items:`. Tracks that one material on its
   own counter. See [Items](Configuration-Items).
2. **Groups** - many materials sharing one counter (for example all crops). Selling any member
   advances the shared total. See [Groups](Configuration-Groups).
3. **Default** - a fallback ladder applied to every material not covered above. See the
   default section in [Configuration](Configuration).

**Precedence (when not stacking):** `items` > `groups` > `default`.

When a material belongs to both an `items` ladder and a group, the group's
[stack mode](Stacking-Multipliers) decides whether they combine.

## Currency-agnostic

ProfitMultiplier never moves money itself. It scales the price the shop transaction already
uses, so the multiplier works with any currency your shop pays in (Vault, PlayerPoints,
tokens, gems, and so on). The [currency settings](Currencies) only control how amounts are
displayed in messages.

## What is tracked

- Per-player, per-material sold totals (persisted in `data.yml`).
- A running bonus total per player (how much extra they have earned from multipliers).
- Group totals are derived by summing the sold totals of a group's member materials, so there
  is no separate storage and groups stay in sync automatically.

## Resets

Totals can be reset per player (`/pm reset`), for everyone (`/pm resetall`), or automatically
on a schedule. See [Auto-Reset](Configuration-Auto-Reset).
