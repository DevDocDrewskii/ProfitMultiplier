# Installation

## Requirements

- A Spigot or Paper server, version **1.8 through 26.x**.
- **One supported shop plugin** (this is where the multiplier is applied):
  EconomyShopGUI, EconomyShopGUI-Premium, ShopGUI+, zShop, UltimateShop, or GUIShop.
- **Vault** plus an economy plugin, if your shop uses Vault for payouts.
- Optional: **PlaceholderAPI** for placeholders, and any of
  **Nexo / Oraxen / ItemsAdder / HeadDatabase** for custom menu icons.

None of the optional plugins are required to load ProfitMultiplier. It detects whatever is
installed and hooks it automatically.

## Steps

1. Download `ProfitMultiplier-x.x.x.jar`.
2. Place it in your server's `plugins/` folder.
3. Restart the server (a reload is not recommended for first install).
4. Open `plugins/ProfitMultiplier/config.yml`, adjust to taste, then run `/pm reload`.

## Verify the hook

On startup the console logs which shops were hooked:

```
[ProfitMultiplier] Sell hooks active: [EconomyShopGUI]
```

If you see `No supported shop plugin detected`, install one of the supported shops or check
the [[hooks section|Shops]] in your config. Enable `debug: true` in `config.yml` for verbose
logging while you set things up.

## File layout

```
plugins/ProfitMultiplier/
  config.yml        # ladders, groups, currencies, shop hooks
  lang.yml          # all player-facing messages
  data.yml          # per-player sold totals (do not edit by hand)
  menus/
    sellmulti.yml   # the /sellmulti menu
    groups.yml      # the paginated group browser
```

## Next steps

- Learn the model: [[How It Works|How-It-Works]]
- Set up ladders: [[Items|Configuration-Items]] and [[Groups|Configuration-Groups]]
- Customise the menu: [[Menus|Menus]]
