package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class PriceEventSellHook extends ReflectiveSellHook {

    protected PriceEventSellHook(ProfitMultiplier plugin, SellProcessor processor) {
        super(plugin, processor);
    }

    protected abstract boolean isSell(Object event) throws Exception;

    protected abstract Player extractPlayer(Object event) throws Exception;

    protected abstract Material extractMaterial(Object event) throws Exception;

    protected abstract int extractAmount(Object event) throws Exception;

    protected abstract double extractPrice(Object event) throws Exception;

    protected abstract void applyPrice(Object event, double price) throws Exception;

    @Override
    protected void handleEvent(Event event) throws Exception {
        if (!isSell(event)) return;
        Player player = extractPlayer(event);
        if (player == null) return;
        Material material = extractMaterial(event);
        if (material == null) return;
        int amount = extractAmount(event);
        if (amount <= 0) return;
        double price = extractPrice(event);
        if (price <= 0) return;

        double newPrice = processor.process(player, material, amount, price);
        if (newPrice > price) {
            applyPrice(event, newPrice);
        }
    }
}
