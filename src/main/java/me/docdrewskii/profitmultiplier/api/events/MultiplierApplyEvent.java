package me.docdrewskii.profitmultiplier.api.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MultiplierApplyEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final Material material;
    private final int amount;
    private final long previousTotal;
    private final double originalPrice;
    private double boostedPrice;
    private boolean cancelled;

    public MultiplierApplyEvent(Player player, Material material, int amount,
                                long previousTotal, double originalPrice, double boostedPrice) {
        this.player = player;
        this.material = material;
        this.amount = amount;
        this.previousTotal = previousTotal;
        this.originalPrice = originalPrice;
        this.boostedPrice = boostedPrice;
    }

    public Player getPlayer() {
        return player;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public long getPreviousTotal() {
        return previousTotal;
    }

    public long getNewTotal() {
        return previousTotal + amount;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getBoostedPrice() {
        return boostedPrice;
    }

    public void setBoostedPrice(double boostedPrice) {
        this.boostedPrice = boostedPrice;
    }

    public double getBonus() {
        return boostedPrice - originalPrice;
    }

    public double getEffectiveMultiplier() {
        return originalPrice <= 0 ? 1.0 : boostedPrice / originalPrice;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
