package M5.Part5;

public enum PayloadType {
    CLIENT_CONNECT, // client requesting to connect to server (passing of initialization data
                    // [name])
    CLIENT_ID, // server sending client id
    SYNC_CLIENT, // silent syncing of clients in room
    DISCONNECT, // distinct disconnect action
    ROOM_CREATE,
    ROOM_JOIN,
    ROOM_LEAVE,
    REVERSE,
    MESSAGE // sender and message
}
