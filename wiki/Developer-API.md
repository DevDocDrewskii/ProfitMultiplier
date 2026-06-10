# Developer API

ProfitMultiplier ships a small, stable API in the package
`me.docdrewskii.profitmultiplier.api`. It depends only on Bukkit/Spigot/Paper, so it is safe
to compile against from any plugin.

## Add the dependency (JitPack)

```kotlin
repositories {
    maven("https://jitpack.io")
}
dependencies {
    compileOnly("com.github.DevDocDrewskii:ProfitMultiplier:1.1.0")
}
```

Maven:

```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>

<dependency>
  <groupId>com.github.DevDocDrewskii</groupId>
  <artifactId>ProfitMultiplier</artifactId>
  <version>1.1.0</version>
  <scope>provided</scope>
</dependency>
```

In your own `plugin.yml`:

```yaml
softdepend: [ProfitMultiplier]
```

## Get the API

```java
if (ProfitMultiplierProvider.isAvailable()) {
    ProfitMultiplierAPI api = ProfitMultiplierProvider.get();
    long sold = api.getSold(player.getUniqueId(), Material.DIAMOND);
    double mult = api.getMultiplier(player.getUniqueId(), Material.DIAMOND);
}
```

Or via the services manager:

```java
ProfitMultiplierAPI api = Bukkit.getServicesManager().load(ProfitMultiplierAPI.class);
```

## React to / modify a boosted sale

```java
@EventHandler
public void onBoost(MultiplierApplyEvent event) {
    if (event.getPlayer().hasPermission("myplugin.doubleboost")) {
        event.setBoostedPrice(event.getBoostedPrice() * 2);
    }
    // event.setCancelled(true); // veto the boost
}
```

GUIShop commits its price at quote time, so its sales do not fire the cancellable
`MultiplierApplyEvent`. All other shops do.

## Other events

| Event | When |
|-------|------|
| `MultiplierApplyEvent` | Before a boosted price is committed (cancellable, settable). |
| `ThresholdReachedEvent` | A sale pushes the player into a new tier. |
| `PlayerDataResetEvent` | One player's data is reset (`getCause()`). |
| `ServerDataResetEvent` | All data is reset. |

`ResetCause` values: `COMMAND`, `AUTOMATIC`, `API`, `UNKNOWN`.

## Selected API methods

`getSold`, `getTotalSold`, `getAllSold`, `getMultiplier`, `getMultiplierAt`,
`getActiveThreshold`, `getNextThreshold`, `getRemainingToNextThreshold`, `getBonusTotal`,
`getLastBonus`, `addSold`, `setSold`, `addBonus`, `resetPlayer`, `resetAll`, `getLastReset`,
`calculateSaleValue`.

## Full guide

The complete developer guide ships in the repository as
[DEVELOPERS.md](https://github.com/DevDocDrewskii/ProfitMultiplier/blob/master/DEVELOPERS.md).
