package me.docdrewskii.profitmultiplier.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An in-memory representation of one menu definition file (e.g. {@code menus/sellmulti.yml}). */
public class Menu {

    private final String name;          // file name without extension, lower-case
    private final String title;         // raw, pre-placeholder
    private final int size;             // inventory size (multiple of 9)
    private final MenuItem filler;      // may be null
    private final List<MenuItem> items;
    private final String openPermission; // null = anyone
    private final boolean update;        // live-refresh while open

    // Progress / status presentation (used by group+tier bound items).
    private final String statusUnlocked;
    private final String statusLocked;
    private final int barLength;
    private final String barSymbol;
    private final String barComplete;
    private final String barIncomplete;

    // Pagination: slots that hold dynamically generated entries, the template used to
    // render each entry, and where the entries come from (e.g. "groups", "tiers:crops").
    private final List<Integer> contentSlots;
    private final MenuItem contentTemplate;
    private final String contentSource;

    // Resolved slot -> item, with filler already excluded.
    private final Map<Integer, MenuItem> slotMap;

    public Menu(String name, String title, int size, MenuItem filler, List<MenuItem> items,
                String openPermission, boolean update, String statusUnlocked, String statusLocked,
                int barLength, String barSymbol, String barComplete, String barIncomplete,
                List<Integer> contentSlots, MenuItem contentTemplate, String contentSource) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.filler = filler;
        this.items = items;
        this.openPermission = openPermission;
        this.update = update;
        this.statusUnlocked = statusUnlocked;
        this.statusLocked = statusLocked;
        this.barLength = barLength;
        this.barSymbol = barSymbol;
        this.barComplete = barComplete;
        this.barIncomplete = barIncomplete;
        this.contentSlots = contentSlots;
        this.contentTemplate = contentTemplate;
        this.contentSource = contentSource;

        this.slotMap = new HashMap<>();
        for (MenuItem item : items) {
            for (int slot : item.getSlots()) {
                if (slot >= 0 && slot < size) {
                    slotMap.put(slot, item);
                }
            }
        }
    }

    public List<Integer> getContentSlots() {
        return contentSlots;
    }

    public MenuItem getContentTemplate() {
        return contentTemplate;
    }

    public String getContentSource() {
        return contentSource;
    }

    /** True when this menu has a working dynamic content area (slots + template + source). */
    public boolean isPaginated() {
        return !contentSlots.isEmpty() && contentTemplate != null && contentSource != null;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public MenuItem getFiller() {
        return filler;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public String getOpenPermission() {
        return openPermission;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getStatusUnlocked() {
        return statusUnlocked;
    }

    public String getStatusLocked() {
        return statusLocked;
    }

    public int getBarLength() {
        return barLength;
    }

    public String getBarSymbol() {
        return barSymbol;
    }

    public String getBarComplete() {
        return barComplete;
    }

    public String getBarIncomplete() {
        return barIncomplete;
    }

    /** The configured item occupying a slot, or {@code null} (filler is not returned here). */
    public MenuItem itemAt(int slot) {
        return slotMap.get(slot);
    }
}
