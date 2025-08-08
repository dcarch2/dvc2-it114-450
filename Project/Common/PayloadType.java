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
    POINTS,
    PICK_CHOICE,
    GAME_START,
    AWAY_STATUS, // DVC2 - 8/6/2025 - New payload type for away status.
    SPECTATE_STATUS; // DVC2 - 8/6/2025 - New payload type for spectator status.
}