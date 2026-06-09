package me.docdrewskii.profitmultiplier.gui.hook;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

/** Reflective bridge to Oraxen's {@code OraxenItems} API. */
public final class OraxenHook {

    private static Boolean available;
    private static Method getItemById;
    private static Method buildMethod;

    private OraxenHook() {
    }

    public static boolean isAvailable() {
        if (available == null) init();
        return available;
    }

    private static void init() {
        available = false;
        if (Bukkit.getPluginManager().getPlugin("Oraxen") == null) return;
        try {
            Class<?> oraxenItems = Class.forName("io.th0rgal.oraxen.api.OraxenItems");
            getItemById = oraxenItems.getMethod("getItemById", String.class);
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }
    }

    public static ItemStack get(String id) {
        if (!isAvailable()) return null;
        try {
            Object builder = getItemById.invoke(null, id);
            if (builder == null) return null;
            if (buildMethod == null) {
                buildMethod = builder.getClass().getMethod("build");
            }
            Object stack = buildMethod.invoke(builder);
            return (stack instanceof ItemStack) ? (ItemStack) stack : null;
        } catch (Throwable t) {
            return null;
        }
    }
}
