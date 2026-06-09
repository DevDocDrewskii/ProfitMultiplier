package me.docdrewskii.profitmultiplier.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class VersionHelper {

    public static final boolean IS_MODERN = isModern();

    private static final Map<String, String> LEGACY_TO_MODERN = new HashMap<>();

    static {
        LEGACY_TO_MODERN.put("GOLD_SWORD",       "GOLDEN_SWORD");
        LEGACY_TO_MODERN.put("GOLD_SPADE",       "GOLDEN_SHOVEL");
        LEGACY_TO_MODERN.put("GOLD_PICKAXE",     "GOLDEN_PICKAXE");
        LEGACY_TO_MODERN.put("GOLD_AXE",         "GOLDEN_AXE");
        LEGACY_TO_MODERN.put("GOLD_HOE",         "GOLDEN_HOE");
        LEGACY_TO_MODERN.put("GOLD_HELMET",      "GOLDEN_HELMET");
        LEGACY_TO_MODERN.put("GOLD_CHESTPLATE",  "GOLDEN_CHESTPLATE");
        LEGACY_TO_MODERN.put("GOLD_LEGGINGS",    "GOLDEN_LEGGINGS");
        LEGACY_TO_MODERN.put("GOLD_BOOTS",       "GOLDEN_BOOTS");
        LEGACY_TO_MODERN.put("GOLD_BARDING",     "GOLDEN_HORSE_ARMOR");
        LEGACY_TO_MODERN.put("IRON_BARDING",     "IRON_HORSE_ARMOR");
        LEGACY_TO_MODERN.put("DIAMOND_BARDING",  "DIAMOND_HORSE_ARMOR");
        LEGACY_TO_MODERN.put("SPADE",            "SHOVEL");
        LEGACY_TO_MODERN.put("WOOD_SWORD",       "WOODEN_SWORD");
        LEGACY_TO_MODERN.put("WOOD_SPADE",       "WOODEN_SHOVEL");
        LEGACY_TO_MODERN.put("WOOD_PICKAXE",     "WOODEN_PICKAXE");
        LEGACY_TO_MODERN.put("WOOD_AXE",         "WOODEN_AXE");
        LEGACY_TO_MODERN.put("WOOD_HOE",         "WOODEN_HOE");
        LEGACY_TO_MODERN.put("STONE_SPADE",      "STONE_SHOVEL");
        LEGACY_TO_MODERN.put("IRON_SPADE",       "IRON_SHOVEL");
        LEGACY_TO_MODERN.put("DIAMOND_SPADE",    "DIAMOND_SHOVEL");
        LEGACY_TO_MODERN.put("WATCH",            "CLOCK");
        LEGACY_TO_MODERN.put("NETHER_BRICK_ITEM","NETHER_BRICK");
        LEGACY_TO_MODERN.put("GRASS",            "SHORT_GRASS");
        LEGACY_TO_MODERN.put("SIGN",             "OAK_SIGN");
        LEGACY_TO_MODERN.put("WOOD",             "OAK_LOG");
        LEGACY_TO_MODERN.put("LOG",              "OAK_LOG");
        LEGACY_TO_MODERN.put("LONG_GRASS",       "SHORT_GRASS");
        LEGACY_TO_MODERN.put("SKULL_ITEM",       "PLAYER_HEAD");
        LEGACY_TO_MODERN.put("TRIAL_KEY",        "TRIAL_KEY");
        LEGACY_TO_MODERN.put("OMINOUS_TRIAL_KEY","OMINOUS_TRIAL_KEY");
        LEGACY_TO_MODERN.put("VAULT",            "VAULT");
        LEGACY_TO_MODERN.put("HEAVY_CORE",       "HEAVY_CORE");
        LEGACY_TO_MODERN.put("MACE",             "MACE");
        LEGACY_TO_MODERN.put("BREEZE_ROD",       "BREEZE_ROD");
        LEGACY_TO_MODERN.put("BUNDLE",           "BUNDLE");
    }

    public static Material resolveMaterial(String name) {
        String upper = name.toUpperCase();

        Material mat = tryParse(upper);
        if (mat != null) return mat;

        if (IS_MODERN) {
            String modern = LEGACY_TO_MODERN.get(upper);
            if (modern != null) return tryParse(modern);
        } else {
            for (Map.Entry<String, String> entry : LEGACY_TO_MODERN.entrySet()) {
                if (entry.getValue().equals(upper)) return tryParse(entry.getKey());
            }
        }

        return null;
    }

    private static Material tryParse(String name) {
        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean isModern() {
        try {
            String raw = Bukkit.getBukkitVersion().split("-")[0];
            String[] parts = raw.split("\\.");
            int major = Integer.parseInt(parts[0]);
            if (major >= 26) return true;
            int minor = Integer.parseInt(parts[1]);
            return minor >= 13;
        } catch (Exception e) {
            return true;
        }
    }
}
