package me.docdrewskii.profitmultiplier.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import me.docdrewskii.profitmultiplier.config.ConfigManager;
import me.docdrewskii.profitmultiplier.data.PlayerDataManager;
import me.docdrewskii.profitmultiplier.model.ItemGroup;
import me.docdrewskii.profitmultiplier.util.NumberUtil;
import me.docdrewskii.profitmultiplier.util.VersionHelper;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.util.UUID;

public class ProfitPlaceholders extends PlaceholderExpansion {

    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.##");
    private static final DecimalFormat MULT = new DecimalFormat("0.##");

    private final ProfitMultiplier plugin;

    public ProfitPlaceholders(ProfitMultiplier plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "profitmultiplier";
    }

    @Override
    public String getAuthor() {
        return "DocDrewskii";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null || params == null) return "";

        UUID id = player.getUniqueId();
        PlayerDataManager pdm = plugin.getDataManager();
        ConfigManager cfg = plugin.getConfigManager();
        String key = params.toLowerCase();

        if (key.equals("sold_total")) {
            return String.valueOf(pdm.getTotalSold(id));
        }
        if (key.equals("bonus_total")) {
            return MONEY.format(pdm.getBonusTotal(id));
        }
        if (key.equals("bonus_last")) {
            return MONEY.format(pdm.getLastBonus(id));
        }
        if (key.equals("bonus_last_display")) {
            double b = pdm.getLastBonus(id);
            if (b <= 0) return "";
            String s = plugin.getLang().get("bonus-display", "{bonus}", MONEY.format(b));
            return s == null ? "" : s;
        }

        if (key.startsWith("sold_")) {
            Material m = VersionHelper.resolveMaterial(params.substring("sold_".length()));
            return m == null ? "0" : String.valueOf(pdm.getSold(id, m));
        }
        if (key.startsWith("multiplier_")) {
            Material m = VersionHelper.resolveMaterial(params.substring("multiplier_".length()));
            if (m == null) return "1";
            return MULT.format(cfg.multiplierAtCount(m, pdm.getSold(id, m)));
        }
        if (key.startsWith("next_threshold_")) {
            Material m = VersionHelper.resolveMaterial(params.substring("next_threshold_".length()));
            if (m == null) return "";
            long next = cfg.nextThresholdAbove(m, pdm.getSold(id, m));
            return next == Long.MAX_VALUE ? "MAX" : String.valueOf(next);
        }
        if (key.startsWith("remaining_")) {
            Material m = VersionHelper.resolveMaterial(params.substring("remaining_".length()));
            if (m == null) return "";
            long soldAmt = pdm.getSold(id, m);
            long next = cfg.nextThresholdAbove(m, soldAmt);
            return next == Long.MAX_VALUE ? "0" : String.valueOf(next - soldAmt);
        }

        // --- Group placeholders: %profitmultiplier_group_<field>_<groupname>% ---
        if (key.startsWith("group_sold_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_sold_".length()));
            return g == null ? "0" : String.valueOf(pdm.getGroupSold(id, g.getMaterials()));
        }
        if (key.startsWith("group_multiplier_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_multiplier_".length()));
            if (g == null) return "1";
            return MULT.format(cfg.groupMultiplierAtCount(g, pdm.getGroupSold(id, g.getMaterials())));
        }
        if (key.startsWith("group_active_threshold_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_active_threshold_".length()));
            if (g == null) return "0";
            return String.valueOf(cfg.groupActiveThreshold(g, pdm.getGroupSold(id, g.getMaterials())));
        }
        if (key.startsWith("group_next_threshold_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_next_threshold_".length()));
            if (g == null) return "";
            long next = cfg.groupNextThresholdAbove(g, pdm.getGroupSold(id, g.getMaterials()));
            return next == Long.MAX_VALUE ? "MAX" : String.valueOf(next);
        }
        if (key.startsWith("group_remaining_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_remaining_".length()));
            if (g == null) return "";
            long soldAmt = pdm.getGroupSold(id, g.getMaterials());
            long next = cfg.groupNextThresholdAbove(g, soldAmt);
            return next == Long.MAX_VALUE ? "0" : String.valueOf(next - soldAmt);
        }
        if (key.startsWith("group_progress_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_progress_".length()));
            if (g == null) return "0";
            long soldAmt = pdm.getGroupSold(id, g.getMaterials());
            long next = cfg.groupNextThresholdAbove(g, soldAmt);
            if (next == Long.MAX_VALUE) return "100";
            return String.valueOf(NumberUtil.percent(soldAmt, next));
        }
        if (key.startsWith("group_max_multiplier_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_max_multiplier_".length()));
            return g == null ? "1" : MULT.format(g.getMaxMultiplier());
        }
        if (key.startsWith("group_tier_count_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_tier_count_".length()));
            return g == null ? "0" : String.valueOf(g.getTiers().size());
        }
        if (key.startsWith("group_display_")) {
            ItemGroup g = cfg.getGroup(params.substring("group_display_".length()));
            if (g == null) return "";
            return g.getDisplayName() != null ? g.getDisplayName() : g.getName();
        }

        return null;
    }
}
