package me.docdrewskii.profitmultiplier.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {

    private final String name;
    private final String title;
    private final int size;
    private final MenuItem filler;
    private final List<MenuItem> items;
    private final String openPermission;
    private final boolean update;

    private final String statusUnlocked;
    private final String statusLocked;
    private final int barLength;
    private final String barSymbol;
    private final String barComplete;
    private final String barIncomplete;

    private final List<Integer> contentSlots;
    private final MenuItem contentTemplate;
    private final String contentSource;

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

    public MenuItem itemAt(int slot) {
        return slotMap.get(slot);
    }
}
