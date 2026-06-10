package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class SellHookManager {

    private final ProfitMultiplier plugin;
    private final SellProcessor processor;
    private final List<String> active = new ArrayList<>();

    public SellHookManager(ProfitMultiplier plugin) {
        this.plugin = plugin;
        this.processor = new SellProcessor(plugin);
    }

    public void registerAll() {
        active.clear();
        ConfigurationSection hooks = plugin.getConfig().getConfigurationSection("hooks");

        if (Bukkit.getPluginManager().getPlugin("EconomyShopGUI") != null
                || Bukkit.getPluginManager().getPlugin("EconomyShopGUI-Premium") != null) {
            register(new EconomyShopGuiHook(plugin, processor), hooks);
        }
        register(new ShopGuiPlusHook(plugin, processor), hooks);
        register(new ZShopHook(plugin, processor), hooks);
        register(new UltimateShopHook(plugin, processor), hooks);
        register(new GuiShopHook(plugin, processor), hooks);
        register(new EssentialsSellHook(plugin, processor), hooks);

        if (active.isEmpty()) {
            plugin.getLogger().warning("No supported shop plugin detected — sell multipliers will not "
                    + "apply. Supported: EconomyShopGUI, ShopGUIPlus, zShop, UltimateShop, GUIShop, Essentials.");
        } else {
            plugin.getLogger().info("Sell hooks active: " + active);
        }
    }

    private void register(SellHook hook, ConfigurationSection hooks) {
        boolean enabled = hooks == null || hooks.getBoolean(hook.pluginName(), true);
        if (!enabled) {
            return;
        }
        try {
            if (hook.register()) {
                active.add(hook.pluginName());
            }
        } catch (Throwable t) {
            plugin.getLogger().warning("Failed to register sell hook " + hook.pluginName() + ": " + t.getMessage());
        }
    }

    public SellProcessor getProcessor() {
        return processor;
    }

    public List<String> getActive() {
        return active;
    }
}
