import java.util.Scanner;

/**
 * Calculator.java
 *
 * <p>A simple command-line calculator that supports four basic arithmetic
 * operations: Addition, Subtraction, Multiplication, and Division.</p>
 *
 * <p>The program runs in an interactive loop, prompting the user to choose
 * an operation and enter two numbers. It continues until the user selects
 * the "Exit" option (choice 5).</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Input validation for both operation choice and numeric values.</li>
 *   <li>Guard against division by zero.</li>
 *   <li>Clean result formatting: integers are shown without a decimal point;
 *       floating-point values retain only significant digits.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <pre>
 *   javac Calculator.java
 *   java Calculator
 * </pre>
 *
 * @author  Jay Gohil
 * @version 1.0
 */
public class Calculator {

    /**
     * Application entry point.
     *
     * <p>Displays a menu of operations in a loop, reads and validates user
     * input, performs the selected arithmetic operation on two user-supplied
     * numbers, and prints the formatted result. The loop exits when the user
     * enters {@code 5} (Exit).</p>
     *
     * <p>Error handling:</p>
     * <ul>
     *   <li>Non-numeric menu choice - prints an error and re-prompts.</li>
     *   <li>Out-of-range menu choice - prints an error and re-prompts.</li>
     *   <li>Non-numeric operand - prints an error and re-prompts.</li>
     *   <li>Division by zero - prints an error and re-prompts.</li>
     * </ul>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Welcome banner ---
        System.out.println("=============================");
        System.out.println("      Simple Calculator      ");
        System.out.println("=============================");

        // --- Main interaction loop: keeps running until the user exits ---
        while (true) {

            // Print the operation menu
            System.out.println("\nSelect an operation:");
            System.out.println("  1. Addition       (+)");
            System.out.println("  2. Subtraction    (-)");
            System.out.println("  3. Multiplication (*)");
            System.out.println("  4. Division       (/)");
            System.out.println("  5. Exit");
            System.out.print("\nEnter your choice (1-5): ");

            // Parse the user's menu choice; re-prompt on invalid (non-integer) input
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                continue;
            }

            // Option 5: exit the program gracefully
            if (choice == 5) {
                System.out.println("Goodbye! Thanks for using Simple Calculator.");
                break;
            }

            // Reject choices outside the valid operation range (1-4)
            if (choice < 1 || choice > 4) {
                System.out.println("Invalid choice! Please select between 1 and 5.");
                continue;
            }

            // Read the two numeric operands; re-prompt on non-numeric input
            double num1, num2;

            try {
                System.out.print("Enter first number  : ");
                num1 = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Enter second number : ");
                num2 = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please enter a valid numeric value.");
                continue;
            }

            // Variables to hold the computed result and the operator symbol for display
            double result;
            String operator;

            // Perform the arithmetic operation selected by the user
            switch (choice) {
                case 1:
                    // Addition
                    result   = num1 + num2;
                    operator = "+";
                    break;
                case 2:
                    // Subtraction
                    result   = num1 - num2;
                    operator = "-";
                    break;
                case 3:
                    // Multiplication
                    result   = num1 * num2;
                    operator = "*";
                    break;
                case 4:
                    // Division -- guard against divide-by-zero before computing
                    if (num2 == 0) {
                        System.out.println("Error: Division by zero is not allowed!");
                        continue;
                    }
                    result   = num1 / num2;
                    operator = "/";
                    break;
                default:
                    // Should never be reached due to the earlier range check
                    System.out.println("Unexpected error.");
                    continue;
            }

            // Print the result; use integer format when result has no fractional part
            if (result == (long) result) {
                System.out.printf("%nResult: %.0f %s %.0f = %.0f%n", num1, operator, num2, result);
            } else {
                System.out.printf("%nResult: %s %s %s = %s%n",
                        formatNumber(num1), operator, formatNumber(num2), formatNumber(result));
            }
        }

        // Release the scanner resource when the loop ends
        scanner.close();
    }

    /**
     * Formats a {@code double} value for cleaner display by removing
     * unnecessary trailing zeros.
     *
     * <p>If {@code value} has no fractional part (e.g. {@code 7.0}), it is
     * returned as a plain integer string (e.g. {@code "7"}). Otherwise the
     * full decimal string representation is returned unchanged.</p>
     *
     * @param value the numeric value to format
     * @return a {@link String} without redundant trailing decimal zeros
     */
    private static String formatNumber(double value) {
        // If the value is a whole number, cast to long to drop the ".0" suffix
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
