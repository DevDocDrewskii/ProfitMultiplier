# Examples & Recipes

Copy-paste starting points. Drop the config blocks into `config.yml` and the menu blocks into
a file under `menus/`, then run `/pm reload`.

The plugin already ships with eight of these built in - Crops, Ores, Resources, Excavation,
Fishing, Nether, Mob Drops, and Building Blocks - so you can study or tweak the live defaults
in `config.yml` as well. The recipes below show how to build your own.

## 1. A simple crops group (the classic 1.1x / 1.2x / 1.3x)

```yaml
groups:
  crops:
    icon: GRASS_BLOCK
    display-name: "Crops"
    materials: [WHEAT, CARROT, POTATO, BEETROOT, KELP, SUGAR_CANE, PUMPKIN, MELON, NETHER_WART]
    tiers:
      - { threshold: 10000,   multiplier: 1.1, icon: GRASS_BLOCK }
      - { threshold: 100000,  multiplier: 1.2, icon: WHEAT }
      - { threshold: 1000000, multiplier: 1.3, icon: KELP }
```

## 2. Per-item ladder for ores

```yaml
items:
  DIAMOND:
    tiers:
      - { threshold: 20,  multiplier: 1.1 }
      - { threshold: 50,  multiplier: 1.25 }
      - { threshold: 100, multiplier: 1.5 }
```

## 3. A group that STACKS on top of item ladders

Diamond keeps its own ladder, and the ores group adds a bonus on top.

```yaml
groups:
  ores:
    display-name: "Ores"
    stack-mode: stack
    materials: [DIAMOND, GOLD_INGOT, IRON_INGOT, EMERALD, COAL]
    tiers:
      - { threshold: 5000,   multiplier: 1.1 }
      - { threshold: 50000,  multiplier: 1.2 }
      - { threshold: 250000, multiplier: 1.3 }
```

See [Stacking Multipliers](Stacking-Multipliers) for the math.

## 4. A group paid in a custom currency

```yaml
currency:
  custom:
    tokens:
      symbol: "tokens "
      position: PREFIX
      format: "#,##0"

groups:
  mob_drops:
    display-name: "Mob Drops"
    currency: tokens
    materials: [ROTTEN_FLESH, BONE, STRING, GUNPOWDER, ENDER_PEARL, BLAZE_ROD]
    tiers:
      - { threshold: 5000,   multiplier: 1.1 }
      - { threshold: 50000,  multiplier: 1.25 }
      - { threshold: 500000, multiplier: 1.5 }
```

## 5. A custom one-row menu

`menus/crops.yml`, opened with `/pm gui crops`:

```yaml
title: "&8» &2Crop Multipliers"
rows: 3
update: true

filler:
  enabled: true
  material: GRAY_STAINED_GLASS_PANE
  name: " "

items:
  info:
    material: "player:{player}"
    slot: 4
    group: crops
    name: "&2{player}'s Crops"
    lore:
      - "&7Sold: &f{sold}"
      - "&7Multiplier: &a{current_multiplier}x"
      - "&7Next tier in &e{remaining_short}"
      - "{progress_bar} &7{progress_percent}%"

  tier1:
    material: GRASS_BLOCK
    slot: 11
    group: crops
    tier: 10000
    name: "&a1.1x"
    lore: [ "&7Sold &f{sold}&7/&f{threshold}", "&7{status}" ]

  tier2:
    material: WHEAT
    slot: 13
    group: crops
    tier: 100000
    name: "&e1.2x"
    lore: [ "&7Sold &f{sold}&7/&f{threshold}", "&7{status}" ]

  tier3:
    material: KELP
    slot: 15
    group: crops
    tier: 1000000
    name: "&b1.3x"
    lore: [ "&7Sold &f{sold}&7/&f{threshold}", "&7{status}" ]

  close:
    material: BARRIER
    slot: 22
    name: "&cClose"
    actions: { any: [ "[close]" ] }
```

## 6. A scoreboard line (PlaceholderAPI)

```
&7Crops: &f%profitmultiplier_group_sold_crops% &8| &a%profitmultiplier_group_multiplier_crops%x
```

## See also

- [Groups](Configuration-Groups)
- [Menus](Menus)
- [PlaceholderAPI](PlaceholderAPI)
