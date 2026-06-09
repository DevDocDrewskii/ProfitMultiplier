package me.docdrewskii.profitmultiplier.gui;

import org.bukkit.event.inventory.ClickType;

import java.util.Locale;

public enum ClickActionType {

    ANY,
    LEFT,
    RIGHT,
    SHIFT_LEFT,
    SHIFT_RIGHT,
    MIDDLE,
    DOUBLE,
    DROP,
    NUMBER_KEY;

    public static ClickActionType fromKey(String key) {
        if (key == null) return null;
        String k = key.trim().toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        switch (k) {
            case "any":
            case "all":
            case "click":
                return ANY;
            case "left":
            case "left_click":
                return LEFT;
            case "right":
            case "right_click":
                return RIGHT;
            case "shift_left":
            case "shiftleft":
            case "left_shift":
                return SHIFT_LEFT;
            case "shift_right":
            case "shiftright":
            case "right_shift":
                return SHIFT_RIGHT;
            case "middle":
            case "middle_click":
                return MIDDLE;
            case "double":
            case "double_click":
                return DOUBLE;
            case "drop":
            case "q":
                return DROP;
            case "number":
            case "number_key":
            case "hotbar":
                return NUMBER_KEY;
            default:
                return null;
        }
    }

    public static ClickActionType fromBukkit(ClickType type) {
        switch (type) {
            case LEFT:
                return LEFT;
            case RIGHT:
                return RIGHT;
            case SHIFT_LEFT:
                return SHIFT_LEFT;
            case SHIFT_RIGHT:
                return SHIFT_RIGHT;
            case MIDDLE:
                return MIDDLE;
            case DOUBLE_CLICK:
                return DOUBLE;
            case DROP:
            case CONTROL_DROP:
                return DROP;
            case NUMBER_KEY:
                return NUMBER_KEY;
            default:
                return ANY;
        }
    }
}
