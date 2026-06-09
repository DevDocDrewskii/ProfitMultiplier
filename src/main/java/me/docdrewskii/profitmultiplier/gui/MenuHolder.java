package me.docdrewskii.profitmultiplier.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuHolder implements InventoryHolder {

    public static final class ClickContext {
        final MenuItem item;
        final Map<String, String> tokens;

        ClickContext(MenuItem item, Map<String, String> tokens) {
            this.item = item;
            this.tokens = tokens;
        }

        public MenuItem getItem() {
            return item;
        }

        public Map<String, String> getTokens() {
            return tokens;
        }
    }

    private final Menu menu;
    private final UUID viewer;
    private Inventory inventory;
    private int page;
    private final Map<Integer, ClickContext> clicks = new HashMap<>();

    public MenuHolder(Menu menu, Player viewer) {
        this.menu = menu;
        this.viewer = viewer.getUniqueId();
        this.page = 0;
    }

    public Menu getMenu() {
        return menu;
    }

    public UUID getViewer() {
        return viewer;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(0, page);
    }

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    void clearClicks() {
        clicks.clear();
    }

    void putClick(int slot, MenuItem item, Map<String, String> tokens) {
        clicks.put(slot, new ClickContext(item, tokens));
    }

    public ClickContext clickAt(int slot) {
        return clicks.get(slot);
    }
}
