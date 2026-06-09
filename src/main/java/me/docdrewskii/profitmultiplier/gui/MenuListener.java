package me.docdrewskii.profitmultiplier.gui;

import me.docdrewskii.profitmultiplier.ProfitMultiplier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;
import java.util.Map;

/** Locks ProfitMultiplier menus (no item movement) and turns clicks into configured actions. */
public class MenuListener implements Listener {

    private final ProfitMultiplier plugin;

    public MenuListener(ProfitMultiplier plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getView().getTopInventory().getHolder();
        if (!(holder instanceof MenuHolder)) {
            return;
        }
        // Lock the whole window: prevents shift-clicking, hotbar swaps, number keys, etc.
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        Inventory clicked = event.getClickedInventory();
        Inventory top = event.getView().getTopInventory();
        if (clicked == null || !clicked.equals(top)) {
            return; // click landed in the player's own inventory
        }

        int slot = event.getRawSlot();
        MenuHolder menuHolder = (MenuHolder) holder;
        MenuHolder.ClickContext ctx = menuHolder.clickAt(slot);
        if (ctx == null) {
            return;
        }

        ClickActionType type = ClickActionType.fromBukkit(event.getClick());
        List<MenuAction> actions = ctx.getItem().actionsFor(type);
        if (actions.isEmpty()) {
            return;
        }

        // Use the exact tokens captured when this slot was rendered (group/tier/page context).
        plugin.getMenuManager().getActionExecutor().run(player, actions, ctx.getTokens());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getView().getTopInventory().getHolder();
        if (holder instanceof MenuHolder) {
            event.setCancelled(true);
        }
    }
}
