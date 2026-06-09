package me.docdrewskii.profitmultiplier.gui.item;

import me.docdrewskii.profitmultiplier.util.VersionHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public final class SkullUtil {

    private SkullUtil() {
    }

    public static ItemStack blankHead() {
        Material mat = VersionHelper.resolveMaterial("PLAYER_HEAD");
        if (mat == null) mat = VersionHelper.resolveMaterial("SKULL_ITEM");
        if (mat == null) return new ItemStack(Material.PUMPKIN);
        if ("SKULL_ITEM".equals(mat.name())) {

            return new ItemStack(mat, 1, (short) 3);
        }
        return new ItemStack(mat, 1);
    }

    public static ItemStack fromPlayer(String name) {
        ItemStack head = blankHead();
        try {
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta == null) return head;
            try {
                OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
                meta.setOwningPlayer(offline);
            } catch (Throwable modernFailed) {

                meta.setOwner(name);
            }
            head.setItemMeta(meta);
        } catch (Throwable ignored) {
        }
        return head;
    }

    public static ItemStack fromBase64(String base64) {
        ItemStack head = blankHead();
        try {
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta == null) return head;

            Class<?> profileClass = Class.forName("com.mojang.authlib.GameProfile");
            Constructor<?> profileCtor = profileClass.getConstructor(UUID.class, String.class);
            Object profile = profileCtor.newInstance(UUID.randomUUID(), null);

            Class<?> propertyClass = Class.forName("com.mojang.authlib.properties.Property");
            Constructor<?> propertyCtor = propertyClass.getConstructor(String.class, String.class);
            Object property = propertyCtor.newInstance("textures", base64);

            Method getProperties = profileClass.getMethod("getProperties");
            Object propertyMap = getProperties.invoke(profile);
            Method put = propertyMap.getClass().getMethod("put", Object.class, Object.class);
            put.invoke(propertyMap, "textures", property);

            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);

            head.setItemMeta(meta);
        } catch (Throwable ignored) {

        }
        return head;
    }
}
