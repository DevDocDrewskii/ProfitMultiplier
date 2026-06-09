package me.docdrewskii.profitmultiplier.model;

import java.util.Locale;

public enum GroupStackMode {

    GROUP,
    ITEM,
    STACK;

    public static GroupStackMode fromString(String raw) {
        if (raw == null) return GROUP;
        switch (raw.trim().toLowerCase(Locale.ROOT)) {
            case "item":
            case "material":
            case "item-priority":
            case "material-priority":
                return ITEM;
            case "stack":
            case "stacked":
            case "both":
            case "multiply":
            case "multiplicative":
                return STACK;
            case "group":
            case "group-priority":
                return GROUP;
            default:
                return GROUP;
        }
    }
}
