package me.docdrewskii.profitmultiplier.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9A-Fa-f]{6})");

    private TextUtil() {
    }

    public static String tokens(String input, Map<String, String> replacements) {
        if (input == null) return "";
        if (replacements == null || replacements.isEmpty()) return input;
        String out = input;
        for (Map.Entry<String, String> e : replacements.entrySet()) {
            out = out.replace("{" + e.getKey() + "}", e.getValue() == null ? "" : e.getValue());
        }
        return out;
    }

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

    public static String color(String input) {
        if (input == null) return "";
        String out = input;
        if (VersionHelper.IS_MODERN && out.indexOf('#') >= 0) {
            out = applyHex(out);
        }
        return ChatColor.translateAlternateColorCodes('&', out);
    }

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

                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group(0)));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
