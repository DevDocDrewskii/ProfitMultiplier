package me.docdrewskii.profitmultiplier.config;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LangManager {

    private final ProfitMultiplier plugin;
    private final File file;

    private final Map<String, String> messages = new HashMap<>();
    private final Map<String, Boolean> enabled = new HashMap<>();
    private String prefix = "";

    public LangManager(ProfitMultiplier plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "lang.yml");
    }

    public void load() {
        if (!file.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);

        messages.clear();
        enabled.clear();
        prefix = yml.getString("prefix", "");

        ConfigurationSection sec = yml.getConfigurationSection("messages");
        if (sec != null) {
            for (String key : sec.getKeys(false)) {
                ConfigurationSection m = sec.getConfigurationSection(key);
                if (m != null) {
                    enabled.put(key, m.getBoolean("enabled", true));
                    messages.put(key, m.getString("message", ""));
                } else {
                    enabled.put(key, true);
                    messages.put(key, sec.getString(key, ""));
                }
            }
        }
    }

    public boolean isActive(String key) {
        Boolean en = enabled.get(key);
        if (en == null || !en) return false;
        String msg = messages.get(key);
        return msg != null && !msg.isEmpty();
    }

    public String get(String key, String... replacements) {
        if (!isActive(key)) return null;
        String msg = messages.get(key).replace("{prefix}", prefix);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        return color(msg);
    }

    public void send(CommandSender to, String key, String... replacements) {
        String msg = get(key, replacements);
        if (msg != null) to.sendMessage(msg);
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
