package me.docdrewskii.profitmultiplier.gui.hook;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

/** Reflective bridge to ItemsAdder's {@code CustomStack} API. */
public final class ItemsAdderHook {

    private static Boolean available;
    private static Method getInstance;
    private static Method getItemStack;

    private ItemsAdderHook() {
    }

    public static boolean isAvailable() {
        if (available == null) init();
        return available;
    }

    private static void init() {
        available = false;
        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) return;
        try {
            Class<?> customStack = Class.forName("dev.lone.itemsadder.api.CustomStack");
            getInstance = customStack.getMethod("getInstance", String.class);
            getItemStack = customStack.getMethod("getItemStack");
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }
    }

    /** @param namespacedId e.g. {@code myitems:ruby} */
    public static ItemStack get(String namespacedId) {
        if (!isAvailable()) return null;
        try {
            Object customStack = getInstance.invoke(null, namespacedId);
            if (customStack == null) return null;
            Object stack = getItemStack.invoke(customStack);
            return (stack instanceof ItemStack) ? (ItemStack) stack : null;
        } catch (Throwable t) {
            return null;
        }
    }
}
