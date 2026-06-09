package me.docdrewskii.profitmultiplier.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Central text pipeline for every menu / message string in the plugin.
 *
 * Order of operations is deliberate:
 *   1. local {token} replacement (per-item context computed in Java)
 *   2. PlaceholderAPI %papi% expansion (only when PlaceholderAPI is installed)
 *   3. &#RRGGBB hex colors (modern servers only)
 *   4. legacy &-codes
 *
 * Doing the PAPI pass BEFORE colorizing means placeholder output that contains
 * &-codes is also translated, which is what admins expect.
 */
public final class TextUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9A-Fa-f]{6})");

    private TextUtil() {
    }

    /** Replace {key} tokens from the given map (null-safe, no-op when map is null/empty). */
    public static String tokens(String input, Map<String, String> replacements) {
        if (input == null) return "";
        if (replacements == null || replacements.isEmpty()) return input;
        String out = input;
        for (Map.Entry<String, String> e : replacements.entrySet()) {
            out = out.replace("{" + e.getKey() + "}", e.getValue() == null ? "" : e.getValue());
        }
        return out;
    }

    /** Expand PlaceholderAPI placeholders when the plugin is present; otherwise a no-op. */
    public static String papi(Player player, String input) {
        if (input == null) return "";
        if (player == null) return input;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return input;
        try {
            return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, input);
        } catch (Throwable t) {
            return input;
        }
    }

    /** Translate &#RRGGBB hex (modern only) and legacy &-codes into rendered colors. */
    public static String color(String input) {
        if (input == null) return "";
        String out = input;
        if (VersionHelper.IS_MODERN && out.indexOf('#') >= 0) {
            out = applyHex(out);
        }
        return ChatColor.translateAlternateColorCodes('&', out);
    }

    /** Full pipeline: tokens -> PAPI -> colors. */
    public static String render(Player player, String input, Map<String, String> tokens) {
        return color(papi(player, tokens(input, tokens)));
    }

    public static List<String> render(Player player, List<String> input, Map<String, String> tokens) {
        List<String> out = new ArrayList<>();
        if (input == null) return out;
        for (String line : input) {
            out.add(render(player, line, tokens));
        }
        return out;
    }

    /** Strip color so we can measure/compare plain text if ever needed. */
    public static String stripColor(String input) {
        return input == null ? "" : ChatColor.stripColor(color(input));
    }

    private static String applyHex(String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            try {
                String replacement = net.md_5.bungee.api.ChatColor.of("#" + hex).toString();
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            } catch (Throwable t) {
                // Server too old for hex (or bungee chat absent) — leave the token untouched.
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
