package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EconomyShopGuiHook implements SellHook, Listener {

    private final ProfitMultiplier plugin;
    private final SellProcessor processor;

    public EconomyShopGuiHook(ProfitMultiplier plugin, SellProcessor processor) {
        this.plugin = plugin;
        this.processor = processor;
    }

    @Override
    public String pluginName() {
        return "EconomyShopGUI";
    }

    @Override
    public boolean register() {
        if (Bukkit.getPluginManager().getPlugin("EconomyShopGUI") == null
                && Bukkit.getPluginManager().getPlugin("EconomyShopGUI-Premium") == null) {
            return false;
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        return true;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onTransaction(PreTransactionEvent event) {
        if (Transaction.Mode.getFromType(event.getTransactionType()) != Transaction.Mode.SELL) {
            return;
        }
        int amount = event.getAmount();
        double price = event.getPrice();
        if (amount <= 0 || price <= 0) return;

        double newPrice = processor.process(event.getPlayer(), event.getItemStack().getType(), amount, price);
        if (newPrice > price) {
            event.setPrice(newPrice);
        }
    }
}
