# FAQ & Troubleshooting

## Sales are not being boosted

1. Check the console on startup for `Sell hooks active: [...]`. If it says
   `No supported shop plugin detected`, install one of the [supported shops](Shops) or check
   your `hooks` toggles.
2. Make sure the item actually has a ladder: an `items` entry, a `group`, or the `default`
   ladder enabled. See [How It Works](How-It-Works).
3. Enable `debug: true` in `config.yml` and sell again. Each transaction the hook sees is
   logged with the computed price.
4. Remember the boost is per unit: early units below the first threshold sell at the base
   rate. The multiplier only shows once a threshold is crossed.

## I updated the plugin but my new groups / menus did not appear

They are merged automatically on startup and on `/pm reload`. New config keys are added to your
existing `config.yml`, new messages to `lang.yml`, and new bundled menu files (like
`groups.yml`) are written to `menus/` if missing. The console logs `Updated config.yml with new
default keys` when this happens. Your existing values and menu files are never overwritten. See
[Updating](Configuration).

## A default group I deleted keeps coming back

The auto-merge is additive: a missing default is treated as "needs adding", so a deleted
default group is re-added on the next load. To suppress a group you do not want, empty its
`materials` list instead of deleting the block.

## I changed a `hooks` value and nothing happened

Shop hooks are bound once at startup. Restart the server after changing a `hooks` toggle.
`/pm reload` reloads config, lang, currencies and menus, but not hooks.

## GUIShop boosts are inconsistent

GUIShop's pricing API has no player context, so the seller is recovered from the GUIShop shop
GUI. Selling through the GUI is reliable. Command-based sells fall back to the un-boosted
price on purpose, rather than guessing the wrong player. See the GUIShop note on [Shops](Shops).

## My custom item icon shows a barrier

The icon string did not resolve. Check the form (`nexo:id`, `oraxen:id`, `itemsadder:ns:id`,
`hdb:1234`) and that the matching plugin is installed. Unknown icons fall back to a barrier so
the mistake is visible. See [Items & Icons](Menus-Items-and-Icons).

## Hex colours are not working

`&#RRGGBB` hex requires a modern server (1.16+). On older servers, use the standard `&` colour
codes.

## The menu title shows `{page}` literally

Inventory titles are fixed when the menu opens and cannot update per page. Put the page count
on a page-info item instead of the title. See [Pagination](Menus-Pagination).

## A message is spamming or I want to silence it

Every message in `lang.yml` has an `enabled` toggle, and is only sent when enabled and
non-empty. Set `enabled: false` or clear the `message` value. See [Messages](Messages).

## Does the multiplier work with non-Vault currencies?

Yes. ProfitMultiplier scales the price the shop transaction already uses, so it works with any
currency your shop pays in. The [currency settings](Currencies) only affect display.

## How do I reset progression for a new season?

Use `/pm resetall`, or enable [auto-reset](Configuration-Auto-Reset) on a schedule.

## Where is player data stored?

In `plugins/ProfitMultiplier/data.yml`. Do not edit it by hand while the server is running.

## Still stuck?

Open an issue with your config, server version, and console log at the
[issue tracker](https://github.com/DevDocDrewskii/ProfitMultiplier/issues).
