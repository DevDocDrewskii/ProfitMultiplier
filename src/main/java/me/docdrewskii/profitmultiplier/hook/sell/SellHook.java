package me.docdrewskii.profitmultiplier.hook.sell;

/**
 * A bridge to one shop/economy plugin's sell pipeline. Implementations detect their plugin,
 * register the appropriate listener, and route each sell through {@link SellProcessor}.
 */
public interface SellHook {

    /** The Bukkit plugin name this hook targets (also the config toggle key under {@code hooks:}). */
    String pluginName();

    /** @return true if the target plugin was present and the hook registered successfully. */
    boolean register();
}
