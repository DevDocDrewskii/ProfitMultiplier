package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class UltimateShopHook extends ReflectiveSellHook {

    public UltimateShopHook(ProfitMultiplier plugin, SellProcessor processor) {
        super(plugin, processor);
    }

    @Override
    public String pluginName() {
        return "UltimateShop";
    }

    @Override
    protected String[] eventClassNames() {
        return new String[]{"cn.superiormc.ultimateshop.api.ItemPreTransactionEvent"};
    }

    @Override
    protected void handleEvent(Event event) throws Exception {

        Object buy = call(event, "isBuyOrSell");
        if (!(buy instanceof Boolean) || (Boolean) buy) return;

        Object playerObj = call(event, "getPlayer");
        if (!(playerObj instanceof Player)) return;
        Player player = (Player) playerObj;

        Material material = resolveMaterial(event, player);
        if (material == null) return;

        Object amountObj = call(event, "getAmount");
        int amount = (amountObj instanceof Number) ? ((Number) amountObj).intValue() : 0;
        if (amount <= 0) return;

        Object giveResult = call(event, "getGiveResult");
        if (giveResult == null) return;

        double originalTotal = sumRewardMap(giveResult);
        if (originalTotal <= 0) return;

        double newTotal = processor.process(player, material, amount, originalTotal);
        if (newTotal > originalTotal) {
            double factor = newTotal / originalTotal;

            call(giveResult, "setMultiplier", factor);
        }
    }

    private Material resolveMaterial(Object event, Player player) {
        try {
            Object item = call(event, "getItem");
            if (item == null) return null;

            Object display = call(item, "getDisplayItem", player);
            if (display instanceof ItemStack) {
                return ((ItemStack) display).getType();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private double sumRewardMap(Object giveResult) {
        try {
            Object map = call(giveResult, "getResultMap");
            if (!(map instanceof Map)) return 0.0;
            double total = 0.0;
            for (Object value : ((Map<?, ?>) map).values()) {
                if (value instanceof Number) total += ((Number) value).doubleValue();
            }
            return total;
        } catch (Throwable t) {
            return 0.0;
        }
    }
}
