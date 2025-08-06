package Project.Server;

import java.util.Collections;
import java.util.Comparator;
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
import Project.Common.PointsPayload;
import Project.Common.RoomAction;
import Project.Common.TextFX;
import Project.Common.TextFX.Color;
import Project.Exceptions.DuplicateRoomException;
import Project.Exceptions.RoomNotFoundException;

/**
 * DVC2 - 7/29/2025 - A Room subclass for the Rock Paper Scissors game. This class manages the game flow, including phases, player choices, battles, and scoring.
 */
public class GameRoom extends Room {
    private final static long ROUND_DURATION_MS = 10000;
    private final ConcurrentHashMap<Long, String> playerChoices = new ConcurrentHashMap<>();
    private String gamePhase = "waiting";
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> roundTimer;

    public GameRoom(String name) {
        super(name);
        info("GameRoom created");
    }

    public synchronized void onSessionStart() { // DVC2 - 7/29/2025 - Changed to public to allow access from ServerThread.
        info(TextFX.colorize("Starting a new game session! Rock, Paper, Scissors, Shoot!", Color.GREEN));
        // Reset player states for a new session
        clientsInRoom.values().forEach(client -> {
            client.getUser().setPoints(0);
            client.getUser().setEliminated(false);
            client.sendResetUserList();
            client.sendClientId();
        });
        // Start the first round
        onRoundStart();
    }

    private synchronized void onRoundStart() {
        gamePhase = "choosing";
        playerChoices.clear();
        info(TextFX.colorize("A new round is starting. Players, please make your choices using /pick [r,p,s]!", Color.YELLOW));

        // Start the round timer
        roundTimer = scheduler.schedule(() -> {
            onRoundEnd();
        }, ROUND_DURATION_MS, TimeUnit.MILLISECONDS);

        // Tell all clients it's their turn to pick
        clientsInRoom.values().stream()
                .filter(client -> !client.getUser().isEliminated())
                .forEach(client -> client.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("It's your turn to pick! Use /pick [r,p,s]", Color.YELLOW)));
    }

    private synchronized void onRoundEnd() {
        gamePhase = "battling";
        // Cancel the timer if the round ended early
        if (roundTimer != null) {
            roundTimer.cancel(false);
        }

        info(TextFX.colorize("Round has ended. Processing results.", Color.RED));
        
        // Eliminate players who didn't make a choice
        clientsInRoom.values().stream()
                .filter(client -> !client.getUser().isEliminated() && !playerChoices.containsKey(client.getClientId()))
                .forEach(client -> {
                    client.getUser().setEliminated(true);
                    relay(null, String.format("%s was eliminated for not making a choice!", client.getDisplayName()));
                });

        // Process battles if more than one player remains
        long remainingPlayers = clientsInRoom.values().stream().filter(c -> !c.getUser().isEliminated()).count();
        if (remainingPlayers > 1) {
            processBattles();
        }

        // Check session end conditions
        onSessionEnd();
    }

    private void processBattles() {
        List<ServerThread> activePlayers = clientsInRoom.values().stream()
                .filter(c -> !c.getUser().isEliminated())
                .collect(Collectors.toList());

        for (int i = 0; i < activePlayers.size(); i++) {
            ServerThread player1 = activePlayers.get(i);
            ServerThread player2 = activePlayers.get((i + 1) % activePlayers.size());

            String choice1 = playerChoices.getOrDefault(player1.getClientId(), "");
            String choice2 = playerChoices.getOrDefault(player2.getClientId(), "");
            
            String outcome = determineWinner(choice1, choice2);

            String message = String.format("%s (%s) vs %s (%s) -> %s",
                    player1.getDisplayName(), choice1, player2.getDisplayName(), choice2, outcome);
            relay(null, message);

            if ("player1".equals(outcome)) {
                player1.getUser().setPoints(player1.getUser().getPoints() + 1);
                player2.getUser().setEliminated(true);
                relay(null, String.format("%s is eliminated!", player2.getDisplayName()));
            } else if ("player2".equals(outcome)) {
                player2.getUser().setPoints(player2.getUser().getPoints() + 1);
                player1.getUser().setEliminated(true);
                relay(null, String.format("%s is eliminated!", player1.getDisplayName()));
            } else {
                relay(null, "It's a tie!");
            }
            // Sync points and elimination status
            player1.sendPoints(player1.getUser().getPoints());
            player2.sendPoints(player2.getUser().getPoints());
        }
    }

    private String determineWinner(String player1, String player2) {
        if (player1.equals(player2)) {
            return "tie";
        }
        if ("r".equals(player1) && "s".equals(player2) ||
            "s".equals(player1) && "p".equals(player2) ||
            "p".equals(player1) && "r".equals(player2)) {
            return "player1";
        }
        return "player2";
    }

    private void onSessionEnd() {
        long remainingPlayers = clientsInRoom.values().stream()
                .filter(c -> !c.getUser().isEliminated())
                .count();

        if (remainingPlayers <= 1) {
            ServerThread winner = clientsInRoom.values().stream()
                    .filter(c -> !c.getUser().isEliminated())
                    .findFirst()
                    .orElse(null);

            if (winner != null) {
                relay(null, TextFX.colorize(String.format("We have a winner! %s wins the game!", winner.getDisplayName()), Color.GREEN));
            } else {
                relay(null, TextFX.colorize("The session is a tie! No winner this time.", Color.RED));
            }
            
            relay(null, TextFX.colorize("\n--- Final Scoreboard ---\n", Color.CYAN));
            clientsInRoom.values().stream()
                    .sorted(Comparator.comparingInt(c -> ((ServerThread) c).getUser().getPoints()).reversed())
                    .forEach(client -> relay(null, String.format("%s: %d points %s", 
                            client.getDisplayName(), client.getUser().getPoints(), 
                            client.getUser().isEliminated() ? "(Eliminated)" : "")));

            relay(null, TextFX.colorize("\n------------------------", Color.CYAN));
            relay(null, "The game is over. A new ready check will be required to start a new session.");
            
            gamePhase = "waiting";

            // Reset client states for a new session without disconnecting
            clientsInRoom.values().forEach(client -> client.getUser().reset());

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
    
    @Override
    protected synchronized void handleMessage(ServerThread sender, String text) {
        // Don't allow general messages during game phase
        if (!gamePhase.equals("waiting")) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("You can only send messages during the 'waiting' phase.", Color.RED));
        } else {
            super.handleMessage(sender, text);
        }
    }

    protected void handlePick(ServerThread sender, String text) {
        if (!gamePhase.equals("choosing")) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("You can only pick during the 'choosing' phase.", Color.RED));
            return;
        }

        if (sender.getUser().isEliminated()) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("You are eliminated and cannot make a choice.", Color.RED));
            return;
        }

        String choice = text.toLowerCase().trim();
        if (!"r".equals(choice) && !"p".equals(choice) && !"s".equals(choice)) {
            sender.sendMessage(Constants.DEFAULT_CLIENT_ID, TextFX.colorize("Invalid choice. Please pick 'r', 'p', or 's'", Color.RED));
            return;
        }
        
        playerChoices.put(sender.getClientId(), choice);
        sender.sendMessage(Constants.DEFAULT_CLIENT_ID, String.format("You have made your choice: %s", choice));
        relay(null, String.format("%s has made their choice.", sender.getDisplayName()));

        // Check if all active players have made a choice
        long activePlayers = clientsInRoom.values().stream().filter(c -> !c.getUser().isEliminated()).count();
        if (playerChoices.size() >= activePlayers) {
            info(TextFX.colorize("All active players have made their choices. Ending round early.", Color.BLUE));
            onRoundEnd();
        }
    }
}