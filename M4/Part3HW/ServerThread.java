
import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private Server server;

    public ServerThread(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendToClient(String sender, String msg) {
        out.println(sender + ": " + msg);
    }

    public void run() {
        try {
            out.println("Enter your username:");
            username = in.readLine();
            server.broadcast("Server", username + " has joined.");

            String input;
            while ((input = in.readLine()) != null) {
                if (input.startsWith("CMD,")) {
                    String[] parts = input.split(",", 2);
                    if (parts.length > 1) {
                        String command = parts[1];
                        // added code for flip command part 1 dvc2 6/23/2025
                        if (command.equals("flip")) {
                            server.handleCoinFlip(username);
                        }
                    }
                } else {
                    server.broadcast(username, input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.broadcast("Server", username + " has left.");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
