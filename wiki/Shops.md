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
| EssentialsX | `/sell` command (inventory diff + worth lookup; bonus paid on top) |

## Enabling and disabling

Toggle each hook in `config.yml`. Keys are the exact plugin names.

```yaml
hooks:
  EconomyShopGUI: true
  ShopGUIPlus: true
  zShop: true
  UltimateShop: true
  GUIShop: true
  Essentials: true
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

## EssentialsX note

EssentialsX has no pre-sell price event, so the multiplier cannot raise the `/sell` payout
itself. Instead, ProfitMultiplier watches `/sell` (including `hand`, `inventory`, `blocks`),
detects what was sold, recomputes the base payout from Essentials' `worth.yml` (including
Essentials' own permission multiplier), and **deposits the bonus on top** through the
Essentials economy. Essentials pays the base amount as usual; ProfitMultiplier pays the
difference one tick later.

This covers the `/sell` command only — Essentials `[Sell]` signs are not boosted. If another
plugin owns the `/sell` command on your server, the hook leaves it alone (it only reacts when
the command resolves to Essentials).

## GUIShop note

GUIShop's pricing API does not pass a player, so ProfitMultiplier recovers the seller from the
GUIShop shop GUI the player is using. Selling **through the GUI** is reliable. Player-less
paths such as command-based sells safely fall back to the un-boosted price rather than
guessing.

## Adding more shops

Support is a thin adapter per shop. If you use a shop that exposes a settable, material-keyed
sell event and want it supported, open an issue on the
[repository](https://github.com/DevDocDrewskii/ProfitMultiplier/issues).
