package me.docdrewskii.profitmultiplier.config;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import me.docdrewskii.profitmultiplier.model.GroupStackMode;
import me.docdrewskii.profitmultiplier.model.ItemGroup;
import me.docdrewskii.profitmultiplier.model.MultiplierTier;
import me.docdrewskii.profitmultiplier.util.VersionHelper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class ConfigManager {

    private final ProfitMultiplier plugin;

    private final Map<Material, List<MultiplierTier>> itemTiers = new HashMap<>();

    /** group name (lower-case) -> group definition. Insertion order preserved for menus. */
    private final Map<String, ItemGroup> groups = new LinkedHashMap<>();
    /** reverse lookup: which group a material belongs to (first group that lists it wins). */
    private final Map<Material, ItemGroup> materialGroup = new HashMap<>();

    private boolean defaultEnabled;
    private int defaultThreshold;
    private double defaultMultiplier;
    private final Set<Material> blacklist = new HashSet<>();

    private boolean autoResetEnabled;
    private long autoResetIntervalMillis;

    private boolean debug;

    public ConfigManager(ProfitMultiplier plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        itemTiers.clear();
        groups.clear();
        materialGroup.clear();
        blacklist.clear();

        ConfigurationSection itemsSection = plugin.getConfig().getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                Material mat = VersionHelper.resolveMaterial(key);
                if (mat == null) {
                    plugin.getLogger().warning("Unknown material in config items section: " + key);
                    continue;
                }
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;

                List<Map<?, ?>> tierList = itemSection.getMapList("tiers");
                List<MultiplierTier> tiers = new ArrayList<>();
                for (Map<?, ?> tierMap : tierList) {
                    Object thresholdObj = tierMap.get("threshold");
                    Object multiplierObj = tierMap.get("multiplier");
                    if (thresholdObj == null || multiplierObj == null) continue;
                    try {
                        int threshold = Integer.parseInt(thresholdObj.toString());
                        double multiplier = Double.parseDouble(multiplierObj.toString());
                        tiers.add(new MultiplierTier(threshold, multiplier));
                    } catch (NumberFormatException e) {
                        plugin.getLogger().warning("Invalid tier values for item " + key);
                    }
                }
                tiers.sort(Comparator.comparingInt(MultiplierTier::getThreshold));
                if (!tiers.isEmpty()) {
                    itemTiers.put(mat, tiers);
                }
            }
        }

        ConfigurationSection groupsSection = plugin.getConfig().getConfigurationSection("groups");
        if (groupsSection != null) {
            for (String key : groupsSection.getKeys(false)) {
                ConfigurationSection groupSection = groupsSection.getConfigurationSection(key);
                if (groupSection == null) continue;

                Set<Material> members = new LinkedHashSet<>();
                for (String matName : groupSection.getStringList("materials")) {
                    Material mat = VersionHelper.resolveMaterial(matName);
                    if (mat == null) {
                        plugin.getLogger().warning("Unknown material '" + matName + "' in group '" + key + "'.");
                        continue;
                    }
                    members.add(mat);
                }

                List<MultiplierTier> tiers = parseTiers(groupSection.getMapList("tiers"), "group " + key);
                tiers.sort(Comparator.comparingInt(MultiplierTier::getThreshold));

                if (members.isEmpty() || tiers.isEmpty()) {
                    plugin.getLogger().warning("Group '" + key + "' has no materials or no tiers — skipped.");
                    continue;
                }

                String icon = groupSection.getString("icon", null);
                String displayName = groupSection.getString("display-name", null);
                GroupStackMode stackMode = GroupStackMode.fromString(
                        groupSection.getString("stack-mode", groupSection.getString("stacking", null)));
                String currency = groupSection.getString("currency", null);

                ItemGroup group = new ItemGroup(key.toLowerCase(Locale.ROOT), members, tiers,
                        icon, displayName, stackMode, currency);
                groups.put(group.getName(), group);
                for (Material mat : members) {
                    materialGroup.putIfAbsent(mat, group);
                }
            }
        }

        ConfigurationSection def = plugin.getConfig().getConfigurationSection("default");
        if (def != null) {
            defaultEnabled = def.getBoolean("enabled", true);
            defaultThreshold = def.getInt("threshold", 1000);
            defaultMultiplier = def.getDouble("multiplier", 1.1);

            for (String key : def.getStringList("blacklist")) {
                Material mat = VersionHelper.resolveMaterial(key);
                if (mat != null) {
                    blacklist.add(mat);
                } else {
                    plugin.getLogger().warning("Unknown material in blacklist: " + key);
                }
            }
        } else {
            defaultEnabled = false;
        }

        ConfigurationSection ar = plugin.getConfig().getConfigurationSection("auto-reset");
        if (ar != null) {
            autoResetEnabled = ar.getBoolean("enabled", false);
            autoResetIntervalMillis = parseInterval(ar.getString("interval", "WEEKLY"));
        } else {
            autoResetEnabled = false;
            autoResetIntervalMillis = 0L;
        }

        debug = plugin.getConfig().getBoolean("debug", false);
    }

    public double computeSaleValue(Material material, long prevTotal, int amount, double basePerUnit) {
        long start = prevTotal + 1;
        long end = prevTotal + amount;
        double total = 0.0;
        long pos = start;
        while (pos <= end) {
            double m = multiplierAtCount(material, pos);
            long next = nextThresholdAbove(material, pos);
            long segEnd = (next == Long.MAX_VALUE) ? end : Math.min(end, next - 1);
            if (segEnd < pos) segEnd = pos;
            long count = segEnd - pos + 1;
            total += count * basePerUnit * m;
            pos = segEnd + 1;
        }
        return total;
    }

    public double multiplierAtCount(Material material, long count) {
        List<MultiplierTier> tiers = itemTiers.get(material);
        if (tiers != null) {
            double best = 1.0;
            for (MultiplierTier t : tiers) {
                if (count >= t.getThreshold()) best = t.getMultiplier();
            }
            return best;
        }
        if (defaultEnabled && !blacklist.contains(material) && count >= defaultThreshold) {
            return defaultMultiplier;
        }
        return 1.0;
    }

    public long activeThreshold(Material material, long count) {
        List<MultiplierTier> tiers = itemTiers.get(material);
        if (tiers != null) {
            long best = 0L;
            for (MultiplierTier t : tiers) {
                if (count >= t.getThreshold()) best = t.getThreshold();
            }
            return best;
        }
        if (defaultEnabled && !blacklist.contains(material) && count >= defaultThreshold) {
            return defaultThreshold;
        }
        return 0L;
    }

    public long nextThresholdAbove(Material material, long count) {
        List<MultiplierTier> tiers = itemTiers.get(material);
        if (tiers != null) {
            for (MultiplierTier t : tiers) {
                if (t.getThreshold() > count) return t.getThreshold();
            }
            return Long.MAX_VALUE;
        }
        if (defaultEnabled && !blacklist.contains(material) && defaultThreshold > count) {
            return defaultThreshold;
        }
        return Long.MAX_VALUE;
    }

    // ---------------------------------------------------------------------
    //  Groups (cumulative multiplier shared across a set of materials)
    // ---------------------------------------------------------------------

    /** True when this material has its own explicit {@code items:} ladder (takes priority over groups). */
    public boolean hasItemTiers(Material material) {
        return itemTiers.containsKey(material);
    }

    /** The group a material belongs to, or {@code null} if none. */
    public ItemGroup getGroupFor(Material material) {
        return materialGroup.get(material);
    }

    public ItemGroup getGroup(String name) {
        return name == null ? null : groups.get(name.toLowerCase(Locale.ROOT));
    }

    public Collection<ItemGroup> getGroups() {
        return groups.values();
    }

    /** Highest unlocked multiplier for a group given the player's cumulative group total. */
    public double groupMultiplierAtCount(ItemGroup group, long count) {
        double best = 1.0;
        for (MultiplierTier t : group.getTiers()) {
            if (count >= t.getThreshold()) best = t.getMultiplier();
        }
        return best;
    }

    /** The threshold of the highest unlocked tier (0 when none reached yet). */
    public long groupActiveThreshold(ItemGroup group, long count) {
        long best = 0L;
        for (MultiplierTier t : group.getTiers()) {
            if (count >= t.getThreshold()) best = t.getThreshold();
        }
        return best;
    }

    /** The next threshold strictly above {@code count}, or {@link Long#MAX_VALUE} when maxed. */
    public long groupNextThresholdAbove(ItemGroup group, long count) {
        for (MultiplierTier t : group.getTiers()) {
            if (t.getThreshold() > count) return t.getThreshold();
        }
        return Long.MAX_VALUE;
    }

    /**
     * Per-unit sale value for a group sale. As the player's cumulative group total
     * climbs from {@code prevTotal+1} to {@code prevTotal+amount}, each unit is priced
     * at the multiplier active at that running total — so a single sale that crosses a
     * tier boundary only boosts the units at/after the boundary.
     */
    /**
     * Sale value for a material that belongs to a group, honouring the group's
     * {@link GroupStackMode}. As the sale proceeds, the per-item counter and the group
     * counter both advance one-per-unit; the effective multiplier is recomputed at every
     * threshold either counter crosses, so a single sale that straddles a boundary only
     * boosts the units at/after it. Segmented (not per-unit) so large sales stay fast.
     */
    public double computeUnifiedSaleValue(Material material, ItemGroup group, GroupStackMode mode,
                                          long prevItem, long prevGroup, int amount, double basePerUnit) {
        double total = 0.0;
        int k = 0;
        while (k < amount) {
            long itemCount = prevItem + k + 1;
            long groupCount = prevGroup + k + 1;

            double materialMult = (mode == GroupStackMode.GROUP) ? 1.0 : multiplierAtCount(material, itemCount);
            double groupMult = (mode == GroupStackMode.ITEM) ? 1.0 : groupMultiplierAtCount(group, groupCount);

            double eff;
            switch (mode) {
                case ITEM:  eff = materialMult; break;
                case GROUP: eff = groupMult; break;
                default:    eff = materialMult * groupMult; break; // STACK
            }

            long remaining = amount - k;
            long itemSpan = (mode == GroupStackMode.GROUP) ? remaining
                    : spanTo(nextThresholdAbove(material, itemCount), itemCount, remaining);
            long groupSpan = (mode == GroupStackMode.ITEM) ? remaining
                    : spanTo(groupNextThresholdAbove(group, groupCount), groupCount, remaining);

            long span = Math.min(itemSpan, groupSpan);
            if (span < 1) span = 1;
            total += span * basePerUnit * eff;
            k += (int) span;
        }
        return total;
    }

    /** Units remaining until {@code currentCount} reaches {@code nextThreshold}, clamped to {@code remaining}. */
    private long spanTo(long nextThreshold, long currentCount, long remaining) {
        if (nextThreshold == Long.MAX_VALUE) return remaining;
        long span = nextThreshold - currentCount;
        if (span < 1) span = 1;
        return Math.min(span, remaining);
    }

    public double computeGroupSaleValue(ItemGroup group, long prevTotal, int amount, double basePerUnit) {
        long start = prevTotal + 1;
        long end = prevTotal + amount;
        double total = 0.0;
        long pos = start;
        while (pos <= end) {
            double m = groupMultiplierAtCount(group, pos);
            long next = groupNextThresholdAbove(group, pos);
            long segEnd = (next == Long.MAX_VALUE) ? end : Math.min(end, next - 1);
            if (segEnd < pos) segEnd = pos;
            long count = segEnd - pos + 1;
            total += count * basePerUnit * m;
            pos = segEnd + 1;
        }
        return total;
    }

    private List<MultiplierTier> parseTiers(List<Map<?, ?>> tierList, String label) {
        List<MultiplierTier> tiers = new ArrayList<>();
        for (Map<?, ?> tierMap : tierList) {
            Object thresholdObj = tierMap.get("threshold");
            Object multiplierObj = tierMap.get("multiplier");
            if (thresholdObj == null || multiplierObj == null) continue;
            try {
                int threshold = Integer.parseInt(thresholdObj.toString());
                double multiplier = Double.parseDouble(multiplierObj.toString());
                Object iconObj = tierMap.get("icon");
                String icon = iconObj == null ? null : iconObj.toString();
                tiers.add(new MultiplierTier(threshold, multiplier, icon));
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid tier values for " + label);
            }
        }
        return tiers;
    }

    private long parseInterval(String s) {
        if (s == null) return 0L;
        s = s.trim();
        try {
            double hours = Double.parseDouble(s);
            return (long) (hours * 3600_000L);
        } catch (NumberFormatException ignored) {
        }
        switch (s.toUpperCase()) {
            case "HOURLY":  return 3600_000L;
            case "DAILY":   return 24L * 3600_000L;
            case "WEEKLY":  return 7L * 24L * 3600_000L;
            case "MONTHLY": return 30L * 24L * 3600_000L;
            default:
                plugin.getLogger().warning("Unknown auto-reset interval '" + s + "', defaulting to WEEKLY.");
                return 7L * 24L * 3600_000L;
        }
    }

    public boolean isAutoResetEnabled() {
        return autoResetEnabled;
    }

    public long getAutoResetIntervalMillis() {
        return autoResetIntervalMillis;
    }

    public boolean isDebug() {
        return debug;
    }
}
