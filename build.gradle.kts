import org.gradle.api.attributes.java.TargetJvmVersion

plugins {
    java
    // Lets JitPack (and `publishToMavenLocal`) publish this project as a dependency
    // other plugins can compile against. See DEVELOPERS.md.
    `maven-publish`
}

group = "me.docdrewskii.profitmultiplier"
version = "1.0.0"

// Build with the installed Java 21 toolchain but emit Java 8 bytecode, so the
// single jar loads on everything from legacy Spigot 1.8 up to Paper 26.x.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    // Ship sources so API consumers get parameter names & Javadoc in their IDE.
    withSourcesJar()
}

repositories {
    mavenCentral()

    // PaperMC — serves paper-api AND its transitives (brigadier, bungeecord-chat).
    // Do NOT restrict to snapshotsOnly: those transitives are release versions.
    maven("https://repo.papermc.io/repository/maven-public/")

    // Mojang libraries — fallback source for com.mojang:brigadier
    maven("https://libraries.minecraft.net/")

    // PlaceholderAPI
    maven("https://repo.helpch.at/releases/")

    maven("https://jitpack.io")
}

// CRITICAL: paper-api's Gradle metadata declares it requires JVM 21+. Because we
// target Java 8 bytecode (above), Gradle's variant matching would otherwise REJECT
// paper-api with a misleading "Could not resolve" error. We override the compile
// classpath to request JVM 21 for resolution only — the emitted bytecode stays at
// Java 8. (Safe here: every dependency is compileOnly, provided by the server.)
configurations.compileClasspath {
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 21)
    }
}

dependencies {
    // Newest Paper API built for Java 21. Transitive deps (adventure, bungee-chat)
    // are pulled automatically — needed just to reference Player/CommandSender.
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    // Vault economy abstraction
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")

    // EconomyShopGUI API (Gypopo) — provides PreTransactionEvent
    compileOnly("com.github.Gypopo:EconomyShopGUI-API:1.7.3")

    // PlaceholderAPI (soft dependency) — optional placeholder expansion
    compileOnly("me.clip:placeholderapi:2.11.7")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        // Silence the "source/target 8 is obsolete" notes from newer JDKs.
        options.compilerArgs.add("-Xlint:-options")
    }

    jar {
        archiveClassifier.set("")
    }
}

// ------------------------------------------------------------------------
//  Standalone API artifacts (the "API file" other developers hook into).
//  Contains ONLY the public me.docdrewskii.profitmultiplier.api package
//  (interface, provider, events, ResetCause) — never the internal impl.
//  Produced in build/libs as:
//    ProfitMultiplier-API-<version>.jar          (classes)
//    ProfitMultiplier-API-<version>-sources.jar  (sources)
//  Attach these to a GitHub Release / SpigotMC resource for manual use.
// ------------------------------------------------------------------------
val apiInclude = "me/docdrewskii/profitmultiplier/api/**"
val apiExclude = "me/docdrewskii/profitmultiplier/api/impl/**"

val apiJar by tasks.registering(Jar::class) {
    archiveBaseName.set("ProfitMultiplier-API")
    archiveClassifier.set("")
    from(sourceSets.main.get().output) {
        include(apiInclude)
        exclude(apiExclude)
    }
}

val apiSourcesJar by tasks.registering(Jar::class) {
    archiveBaseName.set("ProfitMultiplier-API")
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava) {
        include(apiInclude)
        exclude(apiExclude)
    }
}

tasks.named("build") {
    dependsOn(apiJar, apiSourcesJar)
}

// ------------------------------------------------------------------------
//  Publishing — JitPack builds this automatically from a tagged GitHub release.
//  Consumers then add the JitPack repo + this coordinate. See DEVELOPERS.md.
// ------------------------------------------------------------------------
publishing {
    publications {
        create<MavenPublication>("maven") {
            // The plugin jar contains the api package, so consumers compileOnly this.
            // (compileOnly deps like paper-api are NOT added to the published POM.)
            from(components["java"])
        }
    }
}
