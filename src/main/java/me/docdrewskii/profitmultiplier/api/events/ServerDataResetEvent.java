package me.docdrewskii.profitmultiplier.api.events;

import me.docdrewskii.profitmultiplier.api.ResetCause;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerDataResetEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final int playersAffected;
    private final ResetCause cause;

    public ServerDataResetEvent(int playersAffected, ResetCause cause) {
        this.playersAffected = playersAffected;
        this.cause = cause;
    }

    public int getPlayersAffected() {
        return playersAffected;
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
