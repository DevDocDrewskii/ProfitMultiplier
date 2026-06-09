package me.docdrewskii.profitmultiplier.gui;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/** A single configured menu icon: its appearance, optional group/tier binding, and click actions. */
public class MenuItem {

    private final String id;
    private final String icon;                 // raw icon string for ItemResolver
    private final List<Integer> slots;
    private final int amount;
    private final String name;                 // raw, pre-placeholder
    private final List<String> lore;           // raw, pre-placeholder
    private final int customModelData;         // -1 = unset
    private final boolean glow;
    private final List<String> itemFlags;
    private final String viewPermission;        // null = always visible

    private final String group;                 // null = not bound to a group
    private final long tier;                     // -1 = no specific tier threshold
    private final boolean hideOnFirstPage;       // navigation helper (e.g. "previous" button)
    private final boolean hideOnLastPage;        // navigation helper (e.g. "next" button)

    private final Map<ClickActionType, List<MenuAction>> actions;

    public MenuItem(String id, String icon, List<Integer> slots, int amount, String name,
                    List<String> lore, int customModelData, boolean glow, List<String> itemFlags,
                    String viewPermission, String group, long tier,
                    boolean hideOnFirstPage, boolean hideOnLastPage,
                    Map<ClickActionType, List<MenuAction>> actions) {
        this.id = id;
        this.icon = icon;
        this.slots = slots;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.customModelData = customModelData;
        this.glow = glow;
        this.itemFlags = itemFlags;
        this.viewPermission = viewPermission;
        this.group = group;
        this.tier = tier;
        this.hideOnFirstPage = hideOnFirstPage;
        this.hideOnLastPage = hideOnLastPage;
        this.actions = actions;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public boolean isGlow() {
        return glow;
    }

    public List<String> getItemFlags() {
        return itemFlags;
    }

    public String getViewPermission() {
        return viewPermission;
    }

    public String getGroup() {
        return group;
    }

    public long getTier() {
        return tier;
    }

    public boolean isHideOnFirstPage() {
        return hideOnFirstPage;
    }

    public boolean isHideOnLastPage() {
        return hideOnLastPage;
    }

    /** Actions for a fired bucket plus the always-on {@link ClickActionType#ANY} bucket. */
    public List<MenuAction> actionsFor(ClickActionType type) {
        List<MenuAction> out = new ArrayList<>();
        List<MenuAction> any = actions.get(ClickActionType.ANY);
        if (any != null) out.addAll(any);
        if (type != ClickActionType.ANY) {
            List<MenuAction> specific = actions.get(type);
            if (specific != null) out.addAll(specific);
        }
        return out;
    }

    public boolean hasAnyActions() {
        return !actions.isEmpty();
    }

    public static Map<ClickActionType, List<MenuAction>> emptyActions() {
        return new EnumMap<>(ClickActionType.class);
    }
}
