package Project.Server;

import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import Project.Common.TextFX.Color;
import Project.Common.ConnectionPayload;
import Project.Common.Constants;
import Project.Common.LoggerUtil;
import Project.Common.Payload;
import Project.Common.PayloadType;
import Project.Common.PointsPayload;
import Project.Common.RoomAction;
import Project.Common.RoomResultPayload;
import Project.Common.TextFX;

/**
 * A server-side representation of a single client
 */
public class ServerThread extends BaseServerThread {
    private Consumer<ServerThread> onInitializationComplete; // callback to inform when this object is ready

    /**
     * A wrapper method so we don't need to keep typing out the long/complex sysout
     * line inside
     * * @param message
     */
    @Override
    protected void info(String message) {
        LoggerUtil.INSTANCE
                .info(TextFX.colorize(String.format("Thread[%s]: %s", this.getClientId(), message), Color.CYAN));
    }

    /**
     * Wraps the Socket connection and takes a Server reference and a callback
     * * @param myClient
     * @param server
     * @param onInitializationComplete method to inform listener that this object is
     * ready
     */
    protected ServerThread(Socket myClient, Consumer<ServerThread> onInitializationComplete) {
        Objects.requireNonNull(myClient, "Client socket cannot be null");
        Objects.requireNonNull(onInitializationComplete, "callback cannot be null");
        info("ServerThread created");
        // get communication channels to single client
        this.client = myClient;
        // this.clientId = this.threadId(); // An id associated with the thread
        // instance, used as a temporary identifier
        this.onInitializationComplete = onInitializationComplete;

    }

    // Start Send*() Methods
    public boolean sendRooms(List<String> rooms) {
        RoomResultPayload rrp = new RoomResultPayload();
        rrp.setRooms(rooms);
        return sendToClient(rrp);
    }
    
    // DVC2 - 8/6/2025 - `sendPoints` is now managed in the `GameRoom.java` file.
    protected boolean sendPoints(int points) {
        PointsPayload payload = new PointsPayload(this.getClientId(), points);
        return sendToClient(payload);
    }
    
    // DVC2 - 8/6/2025 - Adds `sendPlayerStatus` method for new statuses.
    protected boolean sendPlayerStatus(long clientId, boolean isAway, boolean isSpectator) {
        ConnectionPayload payload = new ConnectionPayload();
        payload.setPayloadType(PayloadType.AWAY_STATUS); // New payload type
        payload.setClientId(clientId);
        payload.setAway(isAway);
        payload.setSpectator(isSpectator);
        return sendToClient(payload);
    }

    // End Send*() Methods

    @Override
    protected void processPayload(Payload incoming) {

        switch (incoming.getPayloadType()) {
            case CLIENT_CONNECT:
                setClientName(((ConnectionPayload) incoming).getClientName().trim());
                break;
            case DISCONNECT:
                currentRoom.handleDisconnect(this);
                break;
            case MESSAGE:
                currentRoom.handleMessage(this, incoming.getMessage());
                break;
            case REVERSE:
                currentRoom.handleReverseText(this, incoming.getMessage());
                break;
            case ROOM_CREATE:
                currentRoom.handleCreateRoom(this, incoming.getMessage());
                break;
            case ROOM_JOIN:
                currentRoom.handleJoinRoom(this, incoming.getMessage());
                break;
            case ROOM_LEAVE:
                currentRoom.handleJoinRoom(this, Room.LOBBY);
                break;
            case ROOM_LIST:
                currentRoom.handleListRooms(this, incoming.getMessage());
                break;
            case PICK_CHOICE:
                if (currentRoom instanceof GameRoom) {
                    ((GameRoom) currentRoom).handlePick(this, incoming.getMessage());
                } else {
                    sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("This command is only available in a game room.", Color.RED));
                }
                break;
            case GAME_START:
                if (currentRoom instanceof GameRoom) {
                    ((GameRoom) currentRoom).onSessionStart();
                } else {
                    sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("This command is only available in a game room.", Color.RED));
                }
                break;
            case AWAY_STATUS: // DVC2 - 8/6/2025 - Handle new away status
                // ... logic to set user's away status in GameRoom
                break;
            case SPECTATE_STATUS: // DVC2 - 8/6/2025 - Handle new spectator status
                // ... logic to set user as spectator in GameRoom
                break;
            default:
                LoggerUtil.INSTANCE.warning(TextFX.colorize("Unknown payload type received", Color.RED));
                break;
        }
    }
    
    @Override
    protected void onInitialized() {
        onInitializationComplete.accept(this);
    }
}