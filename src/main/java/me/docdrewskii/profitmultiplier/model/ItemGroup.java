package me.docdrewskii.profitmultiplier.model;

import org.bukkit.Material;

import java.util.List;
import java.util.Set;

public class ItemGroup {

    private final String name;
    private final Set<Material> materials;
    private final List<MultiplierTier> tiers;
    private final String icon;
    private final String displayName;
    private final GroupStackMode stackMode;
    private final String currency;
    private final MilestoneCommands milestones;

    public ItemGroup(String name, Set<Material> materials, List<MultiplierTier> tiers,
                     String icon, String displayName, GroupStackMode stackMode, String currency) {
        this(name, materials, tiers, icon, displayName, stackMode, currency, null);
    }

    public ItemGroup(String name, Set<Material> materials, List<MultiplierTier> tiers,
                     String icon, String displayName, GroupStackMode stackMode, String currency,
                     MilestoneCommands milestones) {
        this.name = name;
        this.materials = materials;
        this.tiers = tiers;
        this.icon = icon;
        this.displayName = displayName;
        this.stackMode = stackMode;
        this.currency = currency;
        this.milestones = milestones;
    }

    public String getName() {
        return name;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public boolean contains(Material material) {
        return materials.contains(material);
    }

    public List<MultiplierTier> getTiers() {
        return tiers;
    }

    public String getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GroupStackMode getStackMode() {
        return stackMode;
    }

    public String getCurrency() {
        return currency;
    }

    public MilestoneCommands getMilestones() {
        return milestones;
    }

    public double getMaxMultiplier() {
        double best = 1.0;
        for (MultiplierTier tier : tiers) {
            if (tier.getMultiplier() > best) best = tier.getMultiplier();
        }
        return best;
    }
}
