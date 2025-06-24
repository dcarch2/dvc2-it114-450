
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner = new Scanner(System.in);

    public void start() throws IOException {
        socket = new Socket("localhost", 3000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        System.out.println("Connected to server. Type /flip to flip a coin or /quit to exit.");

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("/quit")) {
                break;
            }
            // added code for flip command part 1 dvc2 6/23/2025
            if (input.equals("/flip")) {
                out.println("CMD,flip");
            } else {
                out.println(input);
            }
        }

        socket.close();
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        new Client().start();
    }
}
