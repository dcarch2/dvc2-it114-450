package M3;

import java.util.Scanner;
import java.util.Random;

// DVC2 - 6/16/2025 - my code implements the slash-style command handler and accepts user input with specific commands like /greet, /roll, /echo, and /quit. It handles errors and outputs the proper user input with the commands.

public class SlashCommandHandler extends BaseClass {
    private static String ucid = "dvc2"; // <-- update with your UCID

    public static void main(String[] args) {
        printHeader(ucid, 2, "Objective: Implement a simple slash command parser.");

        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();

            // Exit command
            if (input.equalsIgnoreCase("/quit")) {
                System.out.println("Exiting program...");
                break;
            }

            // /greet <name>
            else if (input.toLowerCase().startsWith("/greet ")) {
                String name = input.substring(7).trim();
                if (name.isEmpty()) {
                    System.out.println("Error: Name is missing.");
                } else {
                    System.out.println("Hello, " + name + "!");
                }
            }

            // /roll <num>d<sides>
            else if (input.toLowerCase().startsWith("/roll ")) {
                String[] parts = input.substring(6).split("d");
                if (parts.length == 2) {
                    try {
                        int num = Integer.parseInt(parts[0].trim());
                        int sides = Integer.parseInt(parts[1].trim());
                        if (num <= 0 || sides <= 0) {
                            System.out.println("Error: Numbers must be positive.");
                        } else {
                            int result = rand.nextInt(sides) + 1;
                            System.out.println("Rolled " + num + "d" + sides + " and got " + result + "!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid number format.");
                    }
                } else {
                    System.out.println("Error: Invalid /roll format. Use /roll <num>d<sides>");
                }
            }

            // /echo <message>
            else if (input.toLowerCase().startsWith("/echo ")) {
                String message = input.substring(6).trim();
                System.out.println(message);
            }

            // Unrecognized command
            else {
                System.out.println("Error: Unrecognized command.");
            }
        }

        printFooter(ucid, 2);
        scanner.close();
    }
}