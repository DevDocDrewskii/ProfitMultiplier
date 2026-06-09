package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * zShop (Maxlego08) integration via
 * {@code fr.maxlego08.zshop.api.event.events.ZShopSellEvent} and {@code ...ZShopSellAllEvent}
 * (getPlayer / getItemButton / getAmount / getPrice / setPrice).
 */
public class ZShopHook extends PriceEventSellHook {

    public ZShopHook(ProfitMultiplier plugin, SellProcessor processor) {
        super(plugin, processor);
    }

    @Override
    public String pluginName() {
        return "zShop";
    }

    @Override
    protected String[] eventClassNames() {
        return new String[]{
                "fr.maxlego08.zshop.api.event.events.ZShopSellEvent",
                "fr.maxlego08.zshop.api.event.events.ZShopSellAllEvent"
        };
    }

    @Override
    protected boolean isSell(Object event) {
        return true; // both registered events are sells
    }

    @Override
    protected Player extractPlayer(Object event) throws Exception {
        Object player = call(event, "getPlayer");
        return (player instanceof Player) ? (Player) player : null;
    }

    @Override
    protected Material extractMaterial(Object event) throws Exception {
        Object itemButton = call(event, "getItemButton");
        if (itemButton == null) return null;
        ItemStack stack = tryItemStack(itemButton, "getItemStack", "getItem", "getCustomItemStack", "build");
        if (stack != null) return stack.getType();
        // Fallback: some buttons expose the Material directly.
        try {
            Object material = call(itemButton, "getMaterial");
            if (material instanceof Material) return (Material) material;
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    protected int extractAmount(Object event) throws Exception {
        Object amount = call(event, "getAmount");
        return (amount instanceof Number) ? ((Number) amount).intValue() : 0;
    }

    @Override
    protected double extractPrice(Object event) throws Exception {
        Object price = call(event, "getPrice");
        return (price instanceof Number) ? ((Number) price).doubleValue() : 0.0;
    }

    @Override
    protected void applyPrice(Object event, double price) throws Exception {
        call(event, "setPrice", price);
    }
}
