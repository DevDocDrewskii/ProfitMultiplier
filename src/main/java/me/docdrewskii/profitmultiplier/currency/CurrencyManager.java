package me.docdrewskii.profitmultiplier.currency;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CurrencyManager {

    private final ProfitMultiplier plugin;
    private final Map<String, Currency> currencies = new HashMap<>();
    private Currency defaultCurrency;

    public CurrencyManager(ProfitMultiplier plugin) {
        this.plugin = plugin;
    }

    public void load() {
        currencies.clear();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("currency");
        if (section == null) {
            defaultCurrency = new Currency("default", "$", false, "#,##0.##");
            return;
        }

        defaultCurrency = parse("default", section);
        currencies.put("default", defaultCurrency);

        ConfigurationSection custom = section.getConfigurationSection("custom");
        if (custom != null) {
            for (String key : custom.getKeys(false)) {
                ConfigurationSection c = custom.getConfigurationSection(key);
                if (c == null) continue;
                String name = key.toLowerCase(Locale.ROOT);
                currencies.put(name, parse(name, c));
            }
        }
    }

    private Currency parse(String name, ConfigurationSection section) {
        String symbol = section.getString("symbol", "$");
        boolean suffix = "SUFFIX".equalsIgnoreCase(section.getString("position", "PREFIX"));
        String pattern = section.getString("format", "#,##0.##");
        return new Currency(name, symbol, suffix, pattern);
    }

    public Currency get(String name) {
        if (name == null) return defaultCurrency;
        Currency c = currencies.get(name.toLowerCase(Locale.ROOT));
        return c != null ? c : defaultCurrency;
    }

    public Currency getDefault() {
        return defaultCurrency;
    }
}
