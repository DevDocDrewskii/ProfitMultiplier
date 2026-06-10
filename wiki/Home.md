# ProfitMultiplier

**Reward your grinders.** ProfitMultiplier gives every player a cumulative sell multiplier
that climbs as they sell. The more a player sells, the more each future sale is worth. It
hooks into your existing shop plugin and boosts the sale price at the moment of sale - there
is nothing to migrate.

> Sell 10K crops -> 1.1x, 100K -> 1.2x, 1M -> 1.3x. Every ladder is configurable.

---

## What it does

- **Cumulative progression** - multipliers rise as a player crosses sold-total thresholds and
  stay unlocked forever. The boost is applied per unit, so a single sale that crosses a
  threshold only boosts the units at or after it.
- **Three ladder types** - per-item, shared groups, and a default fallback. See [[How It Works|How-It-Works]].
- **Stacking control** - decide per group whether the group multiplier replaces, defers to, or
  multiplies with a material's own multiplier. See [[Stacking Multipliers|Stacking-Multipliers]].
- **Multi-shop support** - EconomyShopGUI (+ Premium), ShopGUI+, zShop, UltimateShop, GUIShop,
  and EssentialsX `/sell`.
- **Fully configurable GUI** - custom items, PlaceholderAPI, click actions, pagination.
- **Custom currencies** - display formatting per group; multiplier works with any currency.
- **Milestones** - run console commands on tier-up, scale thresholds per donor rank, and
  announce achievements to Discord (embed or Components V2). See [[Milestones & Discord|Milestones]].
- **Auto-updating configs** - new default groups, messages, and menu files are merged into
  existing installs on update; your own values are never overwritten.
- **Developer API** - read progression, react to milestones, modify the boost.

Ships with eight ready-to-use groups out of the box: Crops, Ores, Resources, Excavation,
Fishing, Nether, Mob Drops, and Building Blocks.

---

## Quick links

| I want to... | Go to |
|--------------|-------|
| Install the plugin | [[Installation]] |
| Understand the multiplier system | [[How It Works|How-It-Works]] |
| Set up multiplier ladders | [[Items|Configuration-Items]] / [[Groups|Configuration-Groups]] |
| Edit the menu | [[Menus|Menus]] |
| Use placeholders | [[PlaceholderAPI|PlaceholderAPI]] |
| Hook a shop | [[Supported Shops|Shops]] |
| Build against the API | [[Developer API|Developer-API]] |
| Copy a ready-made setup | [[Examples & Recipes|Examples]] |
| Fix a problem | [[FAQ & Troubleshooting|FAQ]] |

---

## Compatibility

A single jar built as Java 8 bytecode. Runs on Spigot and Paper from **1.8 through 26.x**.
Requires a supported shop plugin and (optionally) Vault and PlaceholderAPI.
