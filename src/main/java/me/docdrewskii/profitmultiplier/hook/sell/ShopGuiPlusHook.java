package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopGuiPlusHook extends PriceEventSellHook {

    public ShopGuiPlusHook(ProfitMultiplier plugin, SellProcessor processor) {
        super(plugin, processor);
    }

    @Override
    public String pluginName() {
        return "ShopGUIPlus";
    }

    @Override
    protected String[] eventClassNames() {
        return new String[]{"net.brcdev.shopgui.event.ShopPreTransactionEvent"};
    }

    @Override
    protected boolean isSell(Object event) throws Exception {
        Object action = call(event, "getShopAction");
        return action != null && action.toString().toUpperCase().contains("SELL");
    }

    @Override
    protected Player extractPlayer(Object event) throws Exception {
        Object player = call(event, "getPlayer");
        return (player instanceof Player) ? (Player) player : null;
    }

    @Override
    protected Material extractMaterial(Object event) throws Exception {
        Object shopItem = call(event, "getShopItem");
        ItemStack stack = tryItemStack(shopItem, "getItem", "getItemStack");
        return stack != null ? stack.getType() : null;
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
