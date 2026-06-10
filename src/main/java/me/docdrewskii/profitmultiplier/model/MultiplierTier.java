package me.docdrewskii.profitmultiplier.model;

import java.util.Collections;
import java.util.List;

public class MultiplierTier {

    private final int threshold;
    private final double multiplier;

    private final String icon;
    private final List<String> commands;

    public MultiplierTier(int threshold, double multiplier) {
        this(threshold, multiplier, null, null);
    }

    public MultiplierTier(int threshold, double multiplier, String icon) {
        this(threshold, multiplier, icon, null);
    }

    public MultiplierTier(int threshold, double multiplier, String icon, List<String> commands) {
        this.threshold = threshold;
        this.multiplier = multiplier;
        this.icon = icon;
        this.commands = commands == null ? Collections.<String>emptyList()
                : Collections.unmodifiableList(commands);
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

    public List<String> getCommands() {
        return commands;
    }
}
