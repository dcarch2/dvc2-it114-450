package Project.Common;

import java.io.Serializable;

public enum PayloadType implements Serializable {
    CLIENT_CONNECT,
    CLIENT_ID,
    SYNC_CLIENT,
    DISCONNECT,
    ROOM_CREATE,
    ROOM_JOIN,
    ROOM_LEAVE,
    REVERSE,
    MESSAGE,
    ROOM_LIST,
    POINTS, // DVC2 - 7/29/2025 - Added for syncing player points.
    PICK_CHOICE, // DVC2 - 7/29/2025 - Added for player choices in games.
    GAME_START; // DVC2 - 7/29/2025 - Added for syncing game session starts.
}