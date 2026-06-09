package me.docdrewskii.profitmultiplier.gui.hook;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public final class HeadDatabaseHook {

    private static Boolean available;
    private static Object apiInstance;
    private static Method getItemHead;

    private HeadDatabaseHook() {
    }

    public static boolean isAvailable() {
        if (available == null) init();
        return available;
    }

    private static void init() {
        available = false;
        if (Bukkit.getPluginManager().getPlugin("HeadDatabase") == null) return;
        try {
            Class<?> apiClass = Class.forName("me.arcaniax.hdb.api.HeadDatabaseAPI");
            getItemHead = apiClass.getMethod("getItemHead", String.class);
            apiInstance = apiClass.getConstructor().newInstance();
            available = true;
        } catch (Throwable ignored) {
            available = false;
        }
    }

    public static ItemStack get(String id) {
        if (!isAvailable()) return null;
        try {
            Object stack = getItemHead.invoke(apiInstance, id);
            return (stack instanceof ItemStack) ? (ItemStack) stack : null;
        } catch (Throwable t) {
            return null;
        }
    }
}
