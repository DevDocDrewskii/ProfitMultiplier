package me.docdrewskii.profitmultiplier.api.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ThresholdReachedEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Material material;
    private final long newTotal;
    private final double previousMultiplier;
    private final double newMultiplier;
    private final long threshold;

    public ThresholdReachedEvent(Player player, Material material, long newTotal,
                                 double previousMultiplier, double newMultiplier, long threshold) {
        this.player = player;
        this.material = material;
        this.newTotal = newTotal;
        this.previousMultiplier = previousMultiplier;
        this.newMultiplier = newMultiplier;
        this.threshold = threshold;
    }

    public Player getPlayer() {
        return player;
    }

    public Material getMaterial() {
        return material;
    }

    public long getNewTotal() {
        return newTotal;
    }

    public double getPreviousMultiplier() {
        return previousMultiplier;
    }

    public double getNewMultiplier() {
        return newMultiplier;
    }

    public long getThreshold() {
        return threshold;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
