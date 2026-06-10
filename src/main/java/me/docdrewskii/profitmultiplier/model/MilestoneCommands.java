package me.docdrewskii.profitmultiplier.model;

import java.util.Collections;
import java.util.List;

public class MilestoneCommands {

    private final List<String> onTier;
    private final List<String> onMaxTier;

    public MilestoneCommands(List<String> onTier, List<String> onMaxTier) {
        this.onTier = Collections.unmodifiableList(onTier);
        this.onMaxTier = Collections.unmodifiableList(onMaxTier);
    }

    public List<String> getOnTier() {
        return onTier;
    }

    public List<String> getOnMaxTier() {
        return onMaxTier;
    }
}
