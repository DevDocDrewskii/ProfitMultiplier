package me.docdrewskii.profitmultiplier.gui.item;

import me.docdrewskii.profitmultiplier.gui.hook.HeadDatabaseHook;
import me.docdrewskii.profitmultiplier.gui.hook.ItemsAdderHook;
import me.docdrewskii.profitmultiplier.gui.hook.NexoHook;
import me.docdrewskii.profitmultiplier.gui.hook.OraxenHook;
import me.docdrewskii.profitmultiplier.util.VersionHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

/**
 * Turns a config "icon string" into an ItemStack. Supported forms:
 *
 *   STONE                     vanilla material (legacy names auto-mapped)
 *   material:STONE            explicit vanilla material
 *   nexo:my_id                Nexo custom item
 *   oraxen:my_id              Oraxen custom item
 *   itemsadder:ns:id          ItemsAdder custom item
 *   hdb:1234                  HeadDatabase head (alias: headdatabase:)
 *   player:Notch              player-skinned head (alias: playerhead:)
 *   basehead:eyJ0ZXh0...      base64-textured head (alias: head:)
 *
 * Unknown / unavailable sources fall back to a barrier (or stone on very old servers)
 * so a mistyped icon is visible in-game rather than silently absent.
 */
public final class ItemResolver {

    private ItemResolver() {
    }

    public static ItemStack resolve(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return fallback();
        }
        String value = raw.trim();
        int colon = value.indexOf(':');
        if (colon > 0) {
            String prefix = value.substring(0, colon).toLowerCase(Locale.ROOT);
            String arg = value.substring(colon + 1).trim();
            switch (prefix) {
                case "material":
                case "minecraft":
                    return vanilla(arg);
                case "nexo": {
                    ItemStack s = NexoHook.get(arg);
                    return s != null ? s : fallback();
                }
                case "oraxen": {
                    ItemStack s = OraxenHook.get(arg);
                    return s != null ? s : fallback();
                }
                case "itemsadder":
                case "ia": {
                    ItemStack s = ItemsAdderHook.get(arg);
                    return s != null ? s : fallback();
                }
                case "hdb":
                case "headdatabase": {
                    ItemStack s = HeadDatabaseHook.get(arg);
                    return s != null ? s : SkullUtil.blankHead();
                }
                case "player":
                case "playerhead":
                    return SkullUtil.fromPlayer(arg);
                case "head":
                case "basehead":
                case "base64":
                    return SkullUtil.fromBase64(arg);
                default:
                    // Unknown prefix — fall through and try the whole string as a material.
                    break;
            }
        }
        return vanilla(value);
    }

    private static ItemStack vanilla(String name) {
        Material mat = VersionHelper.resolveMaterial(name);
        if (mat == null || mat == Material.AIR) return fallback();
        return new ItemStack(mat);
    }

    private static ItemStack fallback() {
        Material mat = VersionHelper.resolveMaterial("BARRIER");
        if (mat == null) mat = Material.STONE;
        return new ItemStack(mat);
    }
}
