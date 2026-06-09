package me.docdrewskii.profitmultiplier.model;

import org.bukkit.Material;

import java.util.List;
import java.util.Set;

/**
 * A named bucket of materials that share a single CUMULATIVE progression ladder.
 *
 * Unlike the per-item {@code items:} ladders (which track each material on its own
 * counter), a group sums the player's sold-totals across every member material and
 * applies one multiplier to ALL of them. This is what powers the "crops" example:
 * selling wheat, carrots, kelp, etc. all push the same crop counter forward, and the
 * unlocked multiplier boosts every crop sale.
 *
 * Admins can define any number of custom groups; each is automatically exposed through
 * PlaceholderAPI ({@code %profitmultiplier_group_*_<name>%}) and can be browsed in a
 * paginated menu via the {@code content-source: groups} GUI feature.
 */
public class ItemGroup {

    private final String name;
    private final Set<Material> materials;
    private final List<MultiplierTier> tiers;
    private final String icon;             // optional menu icon (any ItemResolver form); may be null
    private final String displayName;      // optional pretty name; may be null
    private final GroupStackMode stackMode; // how this group combines with material multipliers
    private final String currency;         // optional currency name for message display; may be null

    public ItemGroup(String name, Set<Material> materials, List<MultiplierTier> tiers,
                     String icon, String displayName, GroupStackMode stackMode, String currency) {
        this.name = name;
        this.materials = materials;
        this.tiers = tiers;
        this.icon = icon;
        this.displayName = displayName;
        this.stackMode = stackMode;
        this.currency = currency;
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

    /** Tiers sorted ascending by threshold. */
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

    /** The highest multiplier any tier in this group grants (1.0 if there are no tiers). */
    public double getMaxMultiplier() {
        double best = 1.0;
        for (MultiplierTier tier : tiers) {
            if (tier.getMultiplier() > best) best = tier.getMultiplier();
        }
        return best;
    }
}
