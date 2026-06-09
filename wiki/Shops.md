# Supported Shops

ProfitMultiplier applies its multiplier inside your shop plugin's sell pipeline. It detects
every supported shop that is installed and hooks it automatically.

## Supported shops

| Shop | How it hooks |
|------|--------------|
| EconomyShopGUI (+ Premium) | Native `PreTransactionEvent` |
| ShopGUI+ | `ShopPreTransactionEvent` (`setPrice`) |
| zShop | `ZShopSellEvent` / `ZShopSellAllEvent` (`setPrice`) |
| UltimateShop | `ItemPreTransactionEvent` (scales the reward) |
| GUIShop | `DynamicPriceProvider` (player recovered from the shop GUI) |

## Enabling and disabling

Toggle each hook in `config.yml`. Keys are the exact plugin names.

```yaml
hooks:
  EconomyShopGUI: true
  ShopGUIPlus: true
  zShop: true
  UltimateShop: true
  GUIShop: true
```

A hook only registers if its plugin is present. Setting a key to `false` skips it. Changing a
`hooks` toggle takes effect after a server restart (hooks bind once at startup).

## Verifying

The console prints which hooks are active on startup:

```
[ProfitMultiplier] Sell hooks active: [ShopGUIPlus]
```

If a sale is not being boosted, enable `debug: true` in `config.yml` to log each transaction
the hook sees.

## GUIShop note

GUIShop's pricing API does not pass a player, so ProfitMultiplier recovers the seller from the
GUIShop shop GUI the player is using. Selling **through the GUI** is reliable. Player-less
paths such as command-based sells safely fall back to the un-boosted price rather than
guessing.

## Adding more shops

Support is a thin adapter per shop. If you use a shop that exposes a settable, material-keyed
sell event and want it supported, open an issue on the
[repository](https://github.com/DevDocDrewskii/ProfitMultiplier/issues).
