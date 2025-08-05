package M5.Part5;

import java.util.Timer;
import java.util.TimerTask;
import M5.Part5.TextFX.Color;

public class GameRoom extends Room {
    private String gamePhase;
    private Timer roundTimer;

    public GameRoom(String name) {
        super(name);
        this.gamePhase = "waiting";
        this.roundTimer = new Timer();
    }

    // GameRoom.java new file and code added - dvc2 8/4/2025
    protected void onSessionStart() {
        info(TextFX.colorize("Starting a new game session", Color.GREEN));
        
        clientsInRoom.values().forEach(client -> {
            
        });

        onRoundStart();
    }
    
    protected void onRoundStart() {
        info(TextFX.colorize("A new round is starting. Players, please make your choices.", Color.YELLOW));
        
        clientsInRoom.values().forEach(client -> {
            
        });
        
        this.gamePhase = "choosing";
        
        roundTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                info(TextFX.colorize("Round timer has expired. Processing results...", Color.RED));
                onRoundEnd();
            }
        }, 15000);
    }
    
    protected void onRoundEnd() {
        info("Round has ended. Processing results.");
        
        clientsInRoom.values().forEach(client -> {
            
        });
        
        long nonEliminatedCount = clientsInRoom.values().stream()
            .filter(client -> !client.isEliminated())
            .count();
        
        if (nonEliminatedCount == 1) {
            info("We have a winner!");
            onSessionEnd();
        } else if (nonEliminatedCount == 0) {
            info("The session is a tie.");
            onSessionEnd();
        } else {
            onRoundStart();
        }
    }
    
    protected void onSessionEnd() {
        info("Game session has ended.");
        
        clientsInRoom.values().forEach(client -> {
            
        });
    
        info("The game is over. A new ready check will be required to start a new session.");
    }
    
    protected synchronized void handlePick(ServerThread sender, String choice) {
        if (!("r".equalsIgnoreCase(choice) || "p".equalsIgnoreCase(choice) || "s".equalsIgnoreCase(choice))) {
            sender.sendMessage("Invalid choice. Please pick 'r', 'p', or 's'");
            return;
        }
        
        clientsInRoom.values().forEach(client -> {
            
        });
        
        relay(null, String.format("%s has made their choice.", sender.getDisplayName()));
    }
}