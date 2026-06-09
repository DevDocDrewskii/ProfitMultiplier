package me.docdrewskii.profitmultiplier.hook.sell;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class ReflectiveSellHook implements SellHook {

    private static final Listener DUMMY_LISTENER = new Listener() {
    };

    protected final ProfitMultiplier plugin;
    protected final SellProcessor processor;
    private final Map<String, Method> methodCache = new HashMap<>();

    protected ReflectiveSellHook(ProfitMultiplier plugin, SellProcessor processor) {
        this.plugin = plugin;
        this.processor = processor;
    }

    protected abstract String[] eventClassNames();

    protected abstract void handleEvent(Event event) throws Exception;

    @Override
    public boolean register() {
        if (Bukkit.getPluginManager().getPlugin(pluginName()) == null) {
            return false;
        }
        int registered = 0;
        for (String className : eventClassNames()) {
            try {
                Class<?> eventClass = Class.forName(className);
                if (!Event.class.isAssignableFrom(eventClass)) continue;
                Bukkit.getPluginManager().registerEvent(
                        eventClass.asSubclass(Event.class), DUMMY_LISTENER, EventPriority.HIGH,
                        (listener, event) -> dispatch(event), plugin, true);
                registered++;
            } catch (ClassNotFoundException notFound) {

            } catch (Throwable t) {
                plugin.getLogger().warning("[" + pluginName() + "] could not hook " + className + ": " + t.getMessage());
            }
        }
        return registered > 0;
    }

    private void dispatch(Event event) {
        try {
            handleEvent(event);
        } catch (Throwable t) {
            if (plugin.getConfigManager().isDebug()) {
                plugin.getLogger().warning("[" + pluginName() + "] sell handling error: " + t);
            }
        }
    }

    protected Object call(Object target, String name, Object... args) throws Exception {
        Method method = findMethod(target.getClass(), name, args.length);
        if (method == null) {
            throw new NoSuchMethodException(target.getClass().getName() + "#" + name + "/" + args.length);
        }
        return method.invoke(target, args);
    }

    protected ItemStack tryItemStack(Object target, String... names) {
        if (target == null) return null;
        for (String name : names) {
            try {
                Method method = findMethod(target.getClass(), name, 0);
                if (method == null) continue;
                Object result = method.invoke(target);
                if (result instanceof ItemStack) return (ItemStack) result;
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private Method findMethod(Class<?> type, String name, int argCount) {
        String key = type.getName() + "#" + name + "/" + argCount;
        Method cached = methodCache.get(key);
        if (cached != null) return cached;
        for (Method method : type.getMethods()) {
            if (method.getName().equals(name) && method.getParameterTypes().length == argCount) {
                method.setAccessible(true);
                methodCache.put(key, method);
                return method;
            }
        }
        return null;
    }
}
