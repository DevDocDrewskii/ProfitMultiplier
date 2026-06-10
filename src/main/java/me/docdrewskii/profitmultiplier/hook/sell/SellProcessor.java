package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import me.docdrewskii.profitmultiplier.api.events.MultiplierApplyEvent;
import me.docdrewskii.profitmultiplier.api.events.ThresholdReachedEvent;
import me.docdrewskii.profitmultiplier.config.ConfigManager;
import me.docdrewskii.profitmultiplier.currency.Currency;
import me.docdrewskii.profitmultiplier.data.PlayerDataManager;
import me.docdrewskii.profitmultiplier.model.GroupStackMode;
import me.docdrewskii.profitmultiplier.model.ItemGroup;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class SellProcessor {

    private static final DecimalFormat MULT_FORMAT = new DecimalFormat("0.##");

    private final ProfitMultiplier plugin;

    public SellProcessor(ProfitMultiplier plugin) {
        this.plugin = plugin;
    }

    public double process(Player player, Material material, int amount, double originalPrice) {
        if (player == null || material == null || amount <= 0) return originalPrice;

        double finalPrice = originalPrice;
        if (originalPrice > 0) {
            double computed = computeBoostedPrice(player, material, amount, originalPrice);
            if (computed > originalPrice) {
                MultiplierApplyEvent applyEvent = new MultiplierApplyEvent(
                        player, material, amount, plugin.getDataManager().getSold(player.getUniqueId(), material),
                        originalPrice, computed);
                plugin.getServer().getPluginManager().callEvent(applyEvent);
                if (!applyEvent.isCancelled() && applyEvent.getBoostedPrice() > originalPrice) {
                    finalPrice = applyEvent.getBoostedPrice();
                }
            }
        }

        recordSale(player, material, amount, originalPrice, finalPrice);
        return finalPrice;
    }

    public double quoteBoostedPrice(Player player, Material material, int amount, double originalPrice) {
        if (player == null || material == null || amount <= 0 || originalPrice <= 0) return originalPrice;
        return computeBoostedPrice(player, material, amount, originalPrice);
    }

    private double computeBoostedPrice(Player player, Material material, int amount, double originalPrice) {
        ConfigManager cfg = plugin.getConfigManager();
        PlayerDataManager pdm = plugin.getDataManager();
        UUID uuid = player.getUniqueId();

        ItemGroup group = cfg.getGroupFor(material);
        long prevItem = pdm.getSold(uuid, material);
        double basePerUnit = originalPrice / amount;
        double scale = cfg.getThresholdScale(player);

        if (group != null) {
            long prevGroup = pdm.getGroupSold(uuid, group.getMaterials());
            return cfg.computeUnifiedSaleValue(material, group, group.getStackMode(),
                    prevItem, prevGroup, amount, basePerUnit, scale);
        }
        return cfg.computeSaleValue(material, prevItem, amount, basePerUnit, scale);
    }

    public void recordSale(Player player, Material material, int amount, double originalPrice, double finalPrice) {
        if (player == null || material == null || amount <= 0) return;

        ConfigManager cfg = plugin.getConfigManager();
        PlayerDataManager pdm = plugin.getDataManager();
        UUID uuid = player.getUniqueId();

        ItemGroup group = cfg.getGroupFor(material);
        long prevItem = pdm.getSold(uuid, material);
        long prevGroup = group != null ? pdm.getGroupSold(uuid, group.getMaterials()) : 0L;

        double bonus = finalPrice - originalPrice;
        if (bonus > 0 && originalPrice > 0) {
            Currency currency = plugin.getCurrencyManager().get(group != null ? group.getCurrency() : null);
            pdm.addBonus(uuid, bonus);

            if (cfg.isDebug()) {
                plugin.getLogger().info(String.format(
                        "[Debug] %s sold %dx %s%s: base=%.2f -> final=%.2f (%.3fx)",
                        player.getName(), amount, material.name(),
                        group != null ? " [" + group.getName() + "/" + group.getStackMode() + "]" : "",
                        originalPrice, finalPrice, finalPrice / originalPrice));
            }

            long totalForMsg = group != null ? prevGroup + amount : prevItem + amount;
            plugin.getLang().send(player, "multiplier-applied",
                    "{amount}", String.valueOf(amount),
                    "{item}", formatName(material.name()),
                    "{base}", currency.format(originalPrice),
                    "{final}", currency.format(finalPrice),
                    "{bonus}", currency.format(bonus),
                    "{currency}", currency.getSymbol(),
                    "{multiplier}", MULT_FORMAT.format(finalPrice / originalPrice),
                    "{total}", String.valueOf(totalForMsg));
        } else {
            pdm.setLastBonus(uuid, 0.0);
        }

        announceThresholds(player, material, amount, group, prevItem, prevGroup);
        pdm.addSold(uuid, material, amount);
    }

    private void announceThresholds(Player player, Material material, int amount,
                                    ItemGroup group, long prevItem, long prevGroup) {
        ConfigManager cfg = plugin.getConfigManager();
        double scale = cfg.getThresholdScale(player);

        if (group != null && group.getStackMode() != GroupStackMode.ITEM) {
            long newGroup = prevGroup + amount;
            double oldG = cfg.groupMultiplierAtCount(group, prevGroup, scale);
            double newG = cfg.groupMultiplierAtCount(group, newGroup, scale);
            if (newG > oldG) {
                long threshold = cfg.groupActiveThreshold(group, newGroup, scale);
                plugin.getServer().getPluginManager().callEvent(new ThresholdReachedEvent(
                        player, material, newGroup, oldG, newG, threshold));
                plugin.getLang().send(player, "group-threshold-reached",
                        "{group}", formatName(group.getDisplayName() != null ? group.getDisplayName() : group.getName()),
                        "{total}", String.valueOf(newGroup),
                        "{multiplier}", MULT_FORMAT.format(newG),
                        "{threshold}", String.valueOf(threshold));
            }
            plugin.getMilestoneManager().handleCrossings(
                    player, material, group, group.getTiers(), prevGroup, newGroup, scale);
        }

        boolean itemLadderActive = (group == null) || group.getStackMode() != GroupStackMode.GROUP;
        if (itemLadderActive) {
            long newItem = prevItem + amount;
            double oldI = cfg.multiplierAtCount(material, prevItem, scale);
            double newI = cfg.multiplierAtCount(material, newItem, scale);
            if (newI > oldI) {
                long threshold = cfg.activeThreshold(material, newItem, scale);
                if (group == null) {
                    plugin.getServer().getPluginManager().callEvent(new ThresholdReachedEvent(
                            player, material, newItem, oldI, newI, threshold));
                }
                plugin.getLang().send(player, "threshold-reached",
                        "{item}", formatName(material.name()),
                        "{total}", String.valueOf(newItem),
                        "{multiplier}", MULT_FORMAT.format(newI),
                        "{threshold}", String.valueOf(threshold));
            }
            plugin.getMilestoneManager().handleCrossings(
                    player, material, null, cfg.getLadderTiers(material), prevItem, newItem, scale);
        }
    }

    private String formatName(String raw) {
        String name = raw.replace('_', ' ').replace('-', ' ');
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(' ');
            }
        }
        return sb.toString().trim();
    }
}
