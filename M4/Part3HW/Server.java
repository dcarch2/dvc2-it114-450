import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private List<ServerThread> clients = new ArrayList<>();

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ServerThread thread = new ServerThread(clientSocket, this);
            clients.add(thread);
            thread.start();
        }
    }

    public void broadcast(String sender, String message) {
        for (ServerThread client : clients) {
            client.sendToClient(sender, message);
        }
    }

    // added code for pm command part 2 dvc2 6/24/2025
    public void sendPrivateMessage(String sender, String target, String msg) {
        for (ServerThread client : clients) {
            if (client.getUsername() != null &&
                (client.getUsername().equals(sender) || client.getUsername().equals(target))) {
                client.sendToClient("PM from " + sender, msg);
            }
        }
    }

    // added code for flip command part 1 dvc2 6/23/2025
    public void handleCoinFlip(String username) {
        String result = Math.random() < 0.5 ? "heads" : "tails";
        String message = username + " flipped a coin and got " + result;
        broadcast("Server", message);
    }

    // part 3 completed with code dvc2 6/23/2025
    public void handleShuffleMessage(String username, String msg) {
        List<Character> chars = new ArrayList<>();
        for (char c : msg.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);
        StringBuilder shuffled = new StringBuilder();
        for (char c : chars) {
            shuffled.append(c);
        }
        broadcast("Server", "Shuffled from " + username + ": " + shuffled.toString());
    }

    public static void main(String[] args) throws IOException {
        new Server().start(3000);
    }
}
