package Project.Common;

import java.io.Serializable;
import java.util.HashMap;

public enum Command implements Serializable {
    QUIT("quit"),
    DISCONNECT("disconnect"),
    LOGOUT("logout"),
    LOGOFF("logoff"),
    REVERSE("reverse"),
    CREATE_ROOM("createroom"),
    LEAVE_ROOM("leaveroom"),
    JOIN_ROOM("joinroom"),
    NAME("name"),
    LIST_USERS("users"),
    LIST_ROOMS("listrooms"),
    PICK("pick"), // DVC2 - 7/29/2025 - Command for picking a move in the Rock Paper Scissors game.
    READY("ready"); // DVC2 - 7/29/2025 - Command to ready up for a new game session.

    private static final HashMap<String, Command> BY_COMMAND = new HashMap<>();
    static {
        for (Command e : values()) {
            BY_COMMAND.put(e.command, e);
        }
    }
    public final String command;

    private Command(String command) {
        this.command = command;
    }

    public static Command stringToCommand(String command) {
        return BY_COMMAND.get(command);
    }
}