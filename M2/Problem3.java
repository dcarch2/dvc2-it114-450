package M2;

public class Problem3 extends BaseClass {
    private static Integer[] array1 = {42, -17, 89, -256, 1024, -4096, 50000, -123456};
    private static Double[] array2 = {3.14159265358979, -2.718281828459, 1.61803398875, -0.5772156649, 0.0000001, -1000000.0};
    private static Float[] array3 = {1.1f, -2.2f, 3.3f, -4.4f, 5.5f, -6.6f, 7.7f, -8.8f};
    private static String[] array4 = {"123", "-456", "789.01", "-234.56", "0.00001", "-99999999"};
    private static Object[] array5 = {-1, 1, 2.0f, -2.0d, "3", "-3.0"};
    private static void bePositive(Object[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfo(arr, arrayNumber);

        // Challenge 1: Make each value positive
        // Challenge 2: Convert the values back to their original data type and assign it to the proper slot of the `output` array
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        Object[] output = new Object[arr.length];
        // Start Solution Edits
        // Step 1: loop through all of the elements in arr dvc2 6/9/2025
        // Step 2: converts each value to the correct type, making it positive and storing in the output dvc2 6/9/2025
        for (int i = 0; i < arr.length; i++) {
            Object value = arr[i];
            switch (arrayNumber) {
                case 1: // Integer
                    output[i] = Math.abs((Integer) value);
                    break;
                case 2: // Double
                    output[i] = Math.abs((Double) value);
                    break;
                case 3: // Float
                    output[i] = Math.abs((Float) value);
                    break;
                case 4: // String to Double
                    output[i] = Math.abs(Double.parseDouble((String) value));
                    break;
                case 5: // Mixed Object -> String -> Double
                    output[i] = Math.abs(Double.parseDouble(value.toString()));
                    break;
            }
        }
        // End Solution Edits
        System.out.println("Output: ");
        printOutputWithType(output);
        System.out.println("");
        System.out.println("______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "dvc2"; // <-- change to your UCID
        // no edits below this line
        printHeader(ucid, 3);
        bePositive(array1, 1);
        bePositive(array2, 2);
        bePositive(array3, 3);
        bePositive(array4, 4);
        bePositive(array5, 5);
        printFooter(ucid, 3);

    }
}