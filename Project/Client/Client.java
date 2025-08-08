package Project.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Project.Client.Interfaces.IClientEvents;
import Project.Client.Interfaces.IConnectionEvents;
import Project.Client.Interfaces.IMessageEvents;
import Project.Client.Interfaces.IPhaseEvent;
import Project.Client.Interfaces.IPointsEvent;
import Project.Client.Interfaces.IReadyEvent;
import Project.Client.Interfaces.IRoomEvents;
import Project.Client.Interfaces.ITimeEvents;
import Project.Client.Interfaces.ITurnEvent;
import Project.Common.Command;
import Project.Common.ConnectionPayload;
import Project.Common.Constants;
import Project.Common.LoggerUtil;
import Project.Common.Payload;
import Project.Common.PayloadType;
import Project.Common.Phase;
import Project.Common.PointsPayload;
import Project.Common.ReadyPayload;
import Project.Common.RoomAction;
import Project.Common.RoomResultPayload;
import Project.Common.TextFX;
import Project.Common.User;
import Project.Common.TextFX.Color;
import Project.Common.TimerPayload;

/**
 * Demoing bi-directional communication between client and server in a
 * multi-client scenario
 */
public enum Client {
    INSTANCE;

    private Socket server = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    final Pattern ipAddressPattern = Pattern
            .compile("/connect\\s+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{3,5})");
    final Pattern localhostPattern = Pattern.compile("/connect\\s+(localhost:\\d{3,5})");
    private volatile boolean isRunning = true; // volatile for thread-safe visibility
    private final ConcurrentHashMap<Long, User> knownClients = new ConcurrentHashMap<Long, User>();
    private User myUser = new User();
    private Phase currentPhase = Phase.READY;
    // callback that updates the UI
    private static List<IClientEvents> events = new ArrayList<IClientEvents>();
    private String currentRoom;

    private void error(String message) {
        LoggerUtil.INSTANCE.severe(TextFX.colorize(String.format("%s", message), Color.RED));
    }

    private Client() {
        LoggerUtil.INSTANCE.info("Client Created");
    }

    public void registerCallback(IClientEvents e) {
        events.add(e);
    }
    
    // DVC2 - 8/6/2025 - Adds `sendAway()` and `sendSpectate()` methods.
    public void sendAway(boolean isAway) throws IOException {
        ConnectionPayload payload = new ConnectionPayload();
        payload.setPayloadType(PayloadType.AWAY_STATUS); // New payload type
        payload.setAway(isAway); // New field in ConnectionPayload
        sendToServer(payload);
    }
    
    public void sendSpectate(boolean isSpectator) throws IOException {
        ConnectionPayload payload = new ConnectionPayload();
        payload.setPayloadType(PayloadType.SPECTATE_STATUS); // New payload type
        payload.setSpectator(isSpectator); // New field in ConnectionPayload
        sendToServer(payload);
    }
    
    // ... (rest of the file as per the provided `matttoegel` baseline) ...

}