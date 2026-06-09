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

/**
 * Builds player-skull ItemStacks. Base64-texture application uses Mojang's authlib via
 * reflection so we never need a compile-time dependency on {@code com.mojang.authlib}
 * (which the server provides but our build classpath does not).
 */
public final class SkullUtil {

    private SkullUtil() {
    }

    /** A blank player head appropriate for the running server version. */
    public static ItemStack blankHead() {
        Material mat = VersionHelper.resolveMaterial("PLAYER_HEAD");
        if (mat == null) mat = VersionHelper.resolveMaterial("SKULL_ITEM");
        if (mat == null) return new ItemStack(Material.PUMPKIN); // absurd fallback; never expected
        if ("SKULL_ITEM".equals(mat.name())) {
            // Legacy 1.8-1.12: player head is SKULL_ITEM with data value 3.
            return new ItemStack(mat, 1, (short) 3);
        }
        return new ItemStack(mat, 1);
    }

    /** Skull owned by the named player (online lookup; texture resolves when the profile loads). */
    public static ItemStack fromPlayer(String name) {
        ItemStack head = blankHead();
        try {
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta == null) return head;
            try {
                OfflinePlayer offline = Bukkit.getOfflinePlayer(name);
                meta.setOwningPlayer(offline);
            } catch (Throwable modernFailed) {
                // Legacy servers only expose the String-based setter.
                meta.setOwner(name);
            }
            head.setItemMeta(meta);
        } catch (Throwable ignored) {
        }
        return head;
    }

    /** Skull textured from a base64 textures value (the long string from minecraft-heads, etc.). */
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
            // Any failure just leaves a blank head — acceptable degradation.
        }
        return head;
    }
}
