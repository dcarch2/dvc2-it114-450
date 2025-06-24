
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

    // added code for flip command part 1 dvc2 6/23/2025
    public void handleCoinFlip(String username) {
        String result = Math.random() < 0.5 ? "heads" : "tails";
        String message = username + " flipped a coin and got " + result;
        broadcast("Server", message);
    }

    public static void main(String[] args) throws IOException {
        new Server().start(3000);
    }
}
