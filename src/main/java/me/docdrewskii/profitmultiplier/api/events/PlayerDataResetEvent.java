package me.docdrewskii.profitmultiplier.api.events;

import me.docdrewskii.profitmultiplier.api.ResetCause;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerDataResetEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final ResetCause cause;

    public PlayerDataResetEvent(UUID playerId, ResetCause cause) {
        this.playerId = playerId;
        this.cause = cause;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public ResetCause getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
