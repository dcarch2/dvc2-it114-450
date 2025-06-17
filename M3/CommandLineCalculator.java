package M3;

/*
 Challenge 1: Command-Line Calculator
 -----------------------------------
 - Accept two numbers and an operator as command-line arguments
 - Supports addition (+) and subtraction (-)
 - Allow integer and floating-point numbers
 - Ensures correct decimal places in output based on input (e.g., 0.1 + 0.2 → 1 decimal place)
 - Display an error for invalid inputs or unsupported operators
 - Capture 5 variations of tests
*/

public class CommandLineCalculator extends BaseClass {
    private static String ucid = "dvc2"; // <-- change to your UCID
// DVC2 6/16/2025 My code allows for both integers and float values. It calculates and displays the result to the proper decimal number based on the input and includes error handling.
    public static void main(String[] args) {
        printHeader(ucid, 1, "Objective: Implement a calculator using command-line arguments.");

        if (args.length != 3) {
            System.out.println("Usage: java M3.CommandLineCalculator <num1> <operator> <num2>");
            printFooter(ucid, 1);
            return;
        }

        try {
            System.out.println("Calculating result...");

            double num1 = Double.parseDouble(args[0]);
            String operator = args[1];
            double num2 = Double.parseDouble(args[2]);
            double result;

            switch (operator) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                default:
                    System.out.println("Unsupported operator. Use + or -.");
                    printFooter(ucid, 1);
                    return;
            }

            int maxDecimalPlaces = Math.max(getDecimalPlaces(args[0]), getDecimalPlaces(args[2]));
            System.out.printf("Result: %." + maxDecimalPlaces + "f\n", result);

        } catch (Exception e) {
            System.out.println("Invalid input. Please ensure correct format and valid numbers.");
        }

        printFooter(ucid, 1);
    }

    private static int getDecimalPlaces(String numStr) {
        if (numStr.contains(".")) {
            return numStr.length() - numStr.indexOf('.') - 1;
        }
        return 0;
    }
}