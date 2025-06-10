package M2;

public class Problem4 extends BaseClass {
    private static String[] array1 = { "hello world!", "java programming", "special@#$%^&characters", "numbers 123 456",
            "mIxEd CaSe InPut!" };
    private static String[] array2 = { "hello world", "java programming", "this is a title case test",
            "capitalize every word", "mixEd CASE input" };
    private static String[] array3 = { "  hello   world  ", "java    programming  ",
            "  extra    spaces  between   words   ",
            "      leading and trailing spaces      ", "multiple      spaces" };
    private static String[] array4 = { "hello world", "java programming", "short", "a", "even" };

    private static void transformText(String[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfoBasic(arr, arrayNumber);

        // Challenge 1: Remove non-alphanumeric characters except spaces
        // Challenge 2: Convert text to Title Case
        // Challenge 3: Trim leading/trailing spaces and remove duplicate spaces
        // Result 1-3: Assign final phrase to `placeholderForModifiedPhrase`
        // Challenge 4 (extra credit): Extract middle 3 characters (beginning starts at middle of phrase),
        // assign to 'placeholderForMiddleCharacters'
        // if not enough characters assign "Not enough characters"
 
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        String placeholderForModifiedPhrase = "";
        String placeholderForMiddleCharacters = "";
        
        for(int i = 0; i <arr.length; i++){
            // Start Solution Edits
            // Step 1: remove non-alphanumeric chars while keeping letters, digits, and spaces dvc2 6/9/2025
            // Step 2: trim leading and trailing whitespace to collapse into multiple spaces dvc2 6/9/2025
            // Step 3: capitalize first letter of each word dvc2 6/9/2025
             String raw = arr[i];

             raw = raw.replaceAll("[^a-zA-Z0-9 ]", "");

             String[] words = raw.split(" ");
             StringBuilder titleCase = new StringBuilder();
             for (String word : words) {
                if (!word.isEmpty()) {
                    titleCase.append(Character.toUpperCase(word.charAt(0)));
                    if (word.length() > 1) {
                        titleCase.append(word.substring(1).toLowerCase());
                    }
                    titleCase.append(" ");
                }
            }

            placeholderForModifiedPhrase = titleCase.toString().trim();
            placeholderForMiddleCharacters = (placeholderForModifiedPhrase.length() > 1)
                    ? placeholderForModifiedPhrase.substring(1, placeholderForModifiedPhrase.length() - 1)
                    : "";
             // End Solution Edits
            System.out.println(String.format("Index[%d] \"%s\" | Middle: \"%s\"",i, placeholderForModifiedPhrase, placeholderForMiddleCharacters));
        }

       

        
        System.out.println("\n______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "dvc2"; // <-- change to your UCID
        // No edits below this line
        printHeader(ucid, 4);

        transformText(array1, 1);
        transformText(array2, 2);
        transformText(array3, 3);
        transformText(array4, 4);
        printFooter(ucid, 4);
    }

}