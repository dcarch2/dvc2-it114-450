package M3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// DVC2 - 6/16/2025 - my code loads a random story for the stories folder and allows the user to fill in placeholders and then finally prints the completed mad libs story with the user input taking the place of the empty placeholders.

public class MadLibsGenerator extends BaseClass {
    private static final String STORIES_FOLDER = "M3/stories";
    private static String ucid = "dvc2"; // <-- change to your UCID

    public static void main(String[] args) {
        printHeader(ucid, 3, "Objective: Implement a Mad Libs generator that replaces placeholders dynamically.");

        Scanner scanner = new Scanner(System.in);
        File folder = new File(STORIES_FOLDER);

        if (!folder.exists() || !folder.isDirectory() || folder.listFiles().length == 0) {
            System.out.println("Error: No stories found in the 'stories' folder.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }

        List<String> lines = new ArrayList<>();
        // Start edits

        // Load a random story file from the folder
        File[] files = folder.listFiles();
        File storyFile = files[new Random().nextInt(files.length)];

        // Read and store story lines
        Scanner storyScanner;
        try {
            storyScanner = new Scanner(storyFile);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Story file not found.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }

        while (storyScanner.hasNextLine()) {
            lines.add(storyScanner.nextLine());
        }
        storyScanner.close();

        // Replace all placeholders in each line
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            while (line.contains("<") && line.contains(">")) {
                int start = line.indexOf('<');
                int end = line.indexOf('>', start);
                if (start != -1 && end != -1) {
                    String placeholder = line.substring(start + 1, end);
                    System.out.print("Enter a " + placeholder + ": ");
                    String userInput = scanner.nextLine().replace(" ", "_");
                    line = line.substring(0, start) + userInput + line.substring(end + 1);
                } else {
                    break;
                }
            }
            lines.set(i, line); // Save the updated line back
        }

        // End edits
        System.out.println("\nYour Completed Mad Libs Story:\n");
        StringBuilder finalStory = new StringBuilder();
        for (String line : lines) {
            finalStory.append(line).append("\n");
        }
        System.out.println(finalStory.toString());

        printFooter(ucid, 3);
        scanner.close();
    }
}