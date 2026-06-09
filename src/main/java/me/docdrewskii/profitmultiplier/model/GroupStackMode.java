package me.docdrewskii.profitmultiplier.model;

import java.util.Locale;

/**
 * How a group's multiplier interacts with the per-material multiplier (an {@code items:}
 * ladder, or the {@code default:} ladder) when a sold material qualifies for both.
 *
 *   GROUP  — the group multiplier applies; the material multiplier is ignored. (default)
 *   ITEM   — the material multiplier applies; the group multiplier is ignored.
 *   STACK  — both apply, multiplied together (e.g. item 1.5x * group 1.2x = 1.8x).
 *
 * For a material that is ONLY in a group (the common case, e.g. crops), every mode behaves
 * identically — the group multiplier is the only one that exists.
 */
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
