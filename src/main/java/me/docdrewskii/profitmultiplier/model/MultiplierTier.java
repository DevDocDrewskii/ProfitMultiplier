package me.docdrewskii.profitmultiplier.model;

public class MultiplierTier {

    private final int threshold;
    private final double multiplier;

    private final String icon;

    public MultiplierTier(int threshold, double multiplier) {
        this(threshold, multiplier, null);
    }

    public MultiplierTier(int threshold, double multiplier, String icon) {
        this.threshold = threshold;
        this.multiplier = multiplier;
        this.icon = icon;
    }

    public int getThreshold() {
        return threshold;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getIcon() {
        return icon;
    }
}
