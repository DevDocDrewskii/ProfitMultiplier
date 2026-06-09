package me.docdrewskii.profitmultiplier.gui.hook;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public final class NexoHook {

    private static Boolean available;
    private static Method itemFromId;
    private static Method buildMethod;

    private NexoHook() {
    }

    public static boolean isAvailable() {
        if (available == null) init();
        return available;
    }

    private static void init() {
        available = false;
        if (Bukkit.getPluginManager().getPlugin("Nexo") == null) return;
        try {
            Class<?> nexoItems = Class.forName("com.nexomc.nexo.api.NexoItems");
            itemFromId = nexoItems.getMethod("itemFromId", String.class);
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }
    }

    public static ItemStack get(String id) {
        if (!isAvailable()) return null;
        try {
            Object builder = itemFromId.invoke(null, id);
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
