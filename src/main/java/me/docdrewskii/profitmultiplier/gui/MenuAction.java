package me.docdrewskii.profitmultiplier.gui;

import java.util.Locale;

public class MenuAction {

    public enum Type {
        MESSAGE,
        BROADCAST,
        CONSOLE,
        PLAYER,
        CLOSE,
        OPEN,
        SOUND,
        REFRESH,
        NEXT_PAGE,
        PREVIOUS_PAGE,
        PAGE
    }

    private final Type type;
    private final String argument;

    private MenuAction(Type type, String argument) {
        this.type = type;
        this.argument = argument;
    }

    public Type getType() {
        return type;
    }

    public String getArgument() {
        return argument;
    }

    public static MenuAction parse(String line) {
        if (line == null) return null;
        String trimmed = line.trim();
        if (trimmed.isEmpty()) return null;

        if (trimmed.charAt(0) == '[') {
            int end = trimmed.indexOf(']');
            if (end > 0) {
                String tag = trimmed.substring(1, end).trim().toLowerCase(Locale.ROOT);
                String rest = trimmed.substring(end + 1).trim();
                Type type = tagToType(tag);
                if (type != null) {
                    return new MenuAction(type, rest);
                }
            }
        }
        return new MenuAction(Type.MESSAGE, trimmed);
    }

    private static Type tagToType(String tag) {
        switch (tag) {
            case "message":
            case "msg":
            case "tell":
                return Type.MESSAGE;
            case "broadcast":
            case "bc":
                return Type.BROADCAST;
            case "console":
            case "console_command":
            case "command":
                return Type.CONSOLE;
            case "player":
            case "player_command":
            case "run":
                return Type.PLAYER;
            case "close":
                return Type.CLOSE;
            case "open":
            case "openmenu":
            case "open_menu":
                return Type.OPEN;
            case "sound":
                return Type.SOUND;
            case "refresh":
            case "update":
                return Type.REFRESH;
            case "next-page":
            case "next_page":
            case "nextpage":
            case "next":
                return Type.NEXT_PAGE;
            case "previous-page":
            case "previous_page":
            case "prevpage":
            case "prev-page":
            case "previous":
            case "prev":
                return Type.PREVIOUS_PAGE;
            case "page":
            case "goto-page":
            case "gotopage":
                return Type.PAGE;
            default:
                return null;
        }
    }
}
