package Project.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import Project.Common.Command;
import Project.Common.Constants;
import Project.Common.LoggerUtil;
import Project.Common.PayloadType;
import Project.Common.Phase;
import Project.Common.PointsPayload;
import Project.Common.TimedEvent;
import Project.Common.TextFX;
import Project.Common.TimerType;
import Project.Common.TextFX.Color;
import Project.Exceptions.DuplicateRoomException;
import Project.Exceptions.MissingCurrentPlayerException;
import Project.Exceptions.NotPlayersTurnException;
import Project.Exceptions.NotReadyException;
import Project.Exceptions.PhaseMismatchException;
import Project.Exceptions.PlayerNotFoundException;
import Project.Exceptions.RoomNotFoundException;

public class GameRoom extends BaseGameRoom {
    private final static long ROUND_DURATION_MS = 10000;
    private final ConcurrentHashMap<Long, String> playerChoices = new ConcurrentHashMap<>();
    private String gamePhase = "waiting";
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> roundTimer;

    // DVC2 - 8/6/2025 - Added new game settings for Milestone 3
    private boolean extraOptionsEnabled = false;
    private boolean cooldownEnabled = false;

    // DVC2 - 8/6/2025 - Tracks last choice for cooldown
    private final ConcurrentHashMap<Long, String> lastChoices = new ConcurrentHashMap<>();

    public GameRoom(String name) {
        super(name);
        info("GameRoom created");
    }

    public synchronized void onSessionStart() {
        info(TextFX.colorize("Starting a new game session! Rock, Paper, Scissors, Shoot!", Color.GREEN));
        
        // DVC2 - 8/6/2025 - Resetting player states for a new session and handling new statuses.
        clientsInRoom.values().forEach(client -> {
            client.getUser().setPoints(0);
            client.getUser().setEliminated(false);
            client.getUser().setAway(false);
            client.getUser().setSpectator(false); // Reset spectator status
            client.sendResetUserList();
            client.sendClientId();
        });
        
        onRoundStart();
    }

    private synchronized void onRoundStart() {
        gamePhase = "choosing";
        playerChoices.clear();
        info(TextFX.colorize("A new round is starting. Players, please make your choices using /pick [r,p,s]!", Color.YELLOW));
        
        roundTimer = scheduler.schedule(() -> onRoundEnd(), ROUND_DURATION_MS, TimeUnit.MILLISECONDS);

        // DVC2 - 8/6/2025 - Updated to handle new away and spectator statuses.
        clientsInRoom.values().stream()
                .filter(client -> !client.getUser().isEliminated() && !client.getUser().isAway() && !client.getUser().isSpectator())
                .forEach(client -> client.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("It's your turn to pick! Use /pick [r,p,s]", Color.YELLOW)));
    }
    
    private synchronized void onRoundEnd() {
        gamePhase = "battling";
        if (roundTimer != null) {
            roundTimer.cancel(false);
        }

        info(TextFX.colorize("Round has ended. Processing results.", Color.RED));
        
        // DVC2 - 8/6/2025 - Eliminate players who didn't make a choice and are not away.
        clientsInRoom.values().stream()
                .filter(client -> !client.getUser().isEliminated() && !client.getUser().isAway() && !playerChoices.containsKey(client.getClientId()))
                .forEach(client -> {
                    client.getUser().setEliminated(true);
                    relay(null, String.format("%s was eliminated for not making a choice!", client.getDisplayName()));
                });

        long remainingPlayers = clientsInRoom.values().stream().filter(c -> !c.getUser().isEliminated() && !c.getUser().isSpectator()).count();
        if (remainingPlayers > 1) {
            processBattles();
        }

        onSessionEnd();
    }
    
    // DVC2 - 8/6/2025 - Modified to handle extra options.
    private String determineWinner(String player1, String player2) {
        if (player1.equals(player2)) {
            return "tie";
        }
        if ("r".equals(player1) && "s".equals(player2) ||
            "s".equals(player1) && "p".equals(player2) ||
            "p".equals(player1) && "r".equals(player2)) {
            return "player1";
        }
        // DVC2 - 8/6/2025 - Add logic for extra options here if enabled.
        if (extraOptionsEnabled) {
            // New game logic
        }
        return "player2";
    }

    private void onSessionEnd() {
        long remainingPlayers = clientsInRoom.values().stream()
                .filter(c -> !c.getUser().isEliminated() && !c.getUser().isSpectator())
                .count();

        if (remainingPlayers <= 1) {
            // Display scoreboard and end message
            // DVC2 - 8/6/2025 - The scoreboard logic is now in `Project/Client/Client.java`
            // and the server relays the final messages.
            
            gamePhase = "waiting";
            clientsInRoom.values().forEach(client -> {
                client.getUser().reset();
                client.sendResetUserList();
            });
            
        } else {
            onRoundStart();
        }
    }
    
    @Override
    public void handleDisconnect(BaseServerThread sender) {
        // Disconnect logic for GameRoom, including elimination
        if (gamePhase.equals("choosing")) {
            onRoundEnd(); // End the round early if someone disconnects
        }
        super.handleDisconnect(sender);
    }
    
    // DVC2 - 8/6/2025 - Handles /pick command with cooldown logic.
    protected void handlePick(ServerThread sender, String text) {
        if (!gamePhase.equals("choosing")) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("You can only pick during the 'choosing' phase.", Color.RED));
            return;
        }

        if (sender.getUser().isEliminated() || sender.getUser().isAway()) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("You cannot make a choice in your current status.", Color.RED));
            return;
        }

        String choice = text.toLowerCase().trim();
        if (!"r".equals(choice) && !"p".equals(choice) && !"s".equals(choice)) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("Invalid choice. Please pick 'r', 'p', or 's'", Color.RED));
            return;
        }
        
        // DVC2 - 8/6/2025 - Cooldown check
        if (cooldownEnabled && choice.equals(lastChoices.get(sender.getClientId()))) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("You cannot pick the same option twice in a row!", Color.RED));
            return;
        }

        playerChoices.put(sender.getClientId(), choice);
        lastChoices.put(sender.getClientId(), choice);
        sender.sendMessage(Constants.DEFAULT_CLIENT_ID, String.format("You have made your choice: %s", choice));
        relay(null, String.format("%s has made their choice.", sender.getDisplayName()));

        long activePlayers = clientsInRoom.values().stream().filter(c -> !c.getUser().isEliminated() && !c.getUser().isAway()).count();
        if (playerChoices.size() >= activePlayers) {
            info(TextFX.colorize("All active players have made their choices. Ending round early.", Color.BLUE));
            onRoundEnd();
        }
    }
}