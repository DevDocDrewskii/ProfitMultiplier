# ProfitMultiplier — Developer API

ProfitMultiplier ships a small, stable public API in the package
`me.docdrewskii.profitmultiplier.api`. Other plugins can read a player's
sell progression, react to milestones, and even adjust the boosted price
before it's applied.

The API package depends on **nothing but Bukkit/Spigot/Paper** — no Vault,
no shop plugin — so it's safe to compile against from any plugin.

---

## 1. The artifacts

`./gradlew build` produces four jars in `build/libs/`:

| File | What it is |
|------|------------|
| `ProfitMultiplier-1.0.0.jar` | the full plugin (goes in `/plugins`) |
| `ProfitMultiplier-1.0.0-sources.jar` | sources for the whole plugin |
| **`ProfitMultiplier-API-1.0.0.jar`** | **the slim API jar** (only the `api` package) |
| `ProfitMultiplier-API-1.0.0-sources.jar` | sources for the slim API jar |

The **API jar** is the "API file" you hand to other developers. It contains
only the interface, the provider, the events and `ResetCause` — never the
internal implementation.

---

## 2. Depending on the API

### Option A — JitPack (recommended)

After you publish a tag on GitHub (see §5), consumers add:

**Gradle (Kotlin DSL)**
```kotlin
repositories {
    maven("https://jitpack.io")
}
dependencies {
    // JitPack groupId is always com.github.<YourGitHubUser>
    // artifactId is your repository name; version is the git tag (or commit).
    compileOnly("com.github.YourUser:ProfitMultiplier:1.0.0")
}
```

**Gradle (Groovy DSL)**
```groovy
repositories { maven { url 'https://jitpack.io' } }
dependencies { compileOnly 'com.github.YourUser:ProfitMultiplier:1.0.0' }
```

**Maven**
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.YourUser</groupId>
  <artifactId>ProfitMultiplier</artifactId>
  <version>1.0.0</version>
  <scope>provided</scope>
</dependency>
```

> Use `compileOnly` / `provided`: the API is supplied at runtime by the
> installed ProfitMultiplier plugin — don't shade it into your jar.

### Option B — Manual jar

Drop `ProfitMultiplier-API-1.0.0.jar` into a `libs/` folder in your project
and reference it:

```kotlin
dependencies {
    compileOnly(files("libs/ProfitMultiplier-API-1.0.0.jar"))
}
```

---

## 3. Tell the server about the dependency

In **your** plugin's `plugin.yml`, so it loads after ProfitMultiplier:

```yaml
softdepend: [ProfitMultiplier]   # or depend: [ProfitMultiplier] if you require it
```

---

## 4. Using the API

### Get the API instance
```java
import me.docdrewskii.profitmultiplier.api.ProfitMultiplierAPI;
import me.docdrewskii.profitmultiplier.api.ProfitMultiplierProvider;

if (ProfitMultiplierProvider.isAvailable()) {
    ProfitMultiplierAPI api = ProfitMultiplierProvider.get();

    long diamondsSold = api.getSold(player.getUniqueId(), Material.DIAMOND);
    double multiplier = api.getMultiplier(player.getUniqueId(), Material.DIAMOND);
    long remaining    = api.getRemainingToNextThreshold(player.getUniqueId(), Material.DIAMOND);
}
```
You can also grab it from Bukkit's services manager:
```java
ProfitMultiplierAPI api =
    Bukkit.getServicesManager().load(ProfitMultiplierAPI.class);
```

### React to / modify a boosted sale
`MultiplierApplyEvent` fires before the boosted price is committed. It's
**cancellable**, and you can override the boosted price:
```java
@EventHandler
public void onBoost(MultiplierApplyEvent event) {
    Player player = event.getPlayer();
    if (player.hasPermission("myplugin.doubleboost")) {
        // stack your own bonus on top
        event.setBoostedPrice(event.getBoostedPrice() * 2);
    }
    // event.setCancelled(true);  // veto the boost entirely
}
```
(Note: GUIShop commits the price at quote time, so its sales don't fire the
cancellable `MultiplierApplyEvent` — all other shops do.)

### React to milestones / resets
```java
@EventHandler
public void onMilestone(ThresholdReachedEvent event) {
    // event.getMaterial(), getNewTotal(), getNewMultiplier(), getThreshold()
}

@EventHandler
public void onReset(PlayerDataResetEvent event) {
    // event.getPlayerId(), event.getCause()  (COMMAND / AUTOMATIC / API / UNKNOWN)
}
```

---

## 5. Publishing your API online

### Route A — JitPack (free, zero infrastructure; what most plugins use)

1. **Put the project on GitHub** (once):
   ```bash
   git init
   git add .
   git commit -m "ProfitMultiplier 1.0.0"
   git branch -M main
   git remote add origin https://github.com/YourUser/ProfitMultiplier.git
   git push -u origin main
   ```
2. **Tag a release** (JitPack builds tags on demand):
   ```bash
   git tag 1.0.0
   git push origin 1.0.0
   ```
   (Or create a Release in the GitHub UI — that also creates the tag.)
3. **Trigger the build**: go to `https://jitpack.io/#YourUser/ProfitMultiplier`,
   click the tag, press **Get it**. JitPack runs the build (using the pinned
   JDK 21 from `jitpack.yml`) and serves the artifact.
4. Share the coordinate from §2 (`com.github.YourUser:ProfitMultiplier:1.0.0`).
   A green build on the JitPack page means it's live.

### Route B — Direct jar download (GitHub Releases / SpigotMC / Polymart)

1. `./gradlew build` → grab `build/libs/ProfitMultiplier-API-1.0.0.jar`
   (and the `-sources` jar).
2. Attach both jars to a **GitHub Release**, or upload them to your
   SpigotMC/Polymart resource page as a "Developer API" download.
3. Developers use **Option B** from §2.

### Route C — Your own Maven repo (optional, advanced)
If you host a repo (Reposilite, Nexus, GitHub Packages), add `credentials`
and a `maven { url = ... }` block under `publishing { repositories { ... } }`
in `build.gradle.kts`, then run `./gradlew publish`.

---

## 6. API reference

**`ProfitMultiplierAPI`** (selected): `getSold`, `getTotalSold`, `getAllSold`,
`getMultiplier`, `getMultiplierAt`, `getActiveThreshold`, `getNextThreshold`,
`getRemainingToNextThreshold`, `getBonusTotal`, `getLastBonus`, `addSold`,
`setSold`, `addBonus`, `resetPlayer`, `resetAll`, `getLastReset`,
`calculateSaleValue`.

**Events** (`...api.events`): `MultiplierApplyEvent` (cancellable, settable
price), `ThresholdReachedEvent`, `PlayerDataResetEvent`, `ServerDataResetEvent`.

**`ProfitMultiplierProvider`**: `get()`, `isAvailable()`.

**PlaceholderAPI** (no API dependency needed): `%profitmultiplier_sold_<mat>%`,
`%profitmultiplier_multiplier_<mat>%`, `%profitmultiplier_group_multiplier_<group>%`,
and more — see `config.yml`.
