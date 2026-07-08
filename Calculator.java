import java.util.Scanner;

/**
 * A simple console-based calculator that supports
 * addition, subtraction, multiplication, and division.
 */
public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Simple Java Calculator ===");

        while (running) {
            System.out.println();
            System.out.println("Choose an operation:");
            System.out.println("  1. Add (+)");
            System.out.println("  2. Subtract (-)");
            System.out.println("  3. Multiply (*)");
            System.out.println("  4. Divide (/)");
            System.out.println("  5. Exit");
            System.out.print("Enter choice (1-5): ");

            String choice = scanner.next();

            if (choice.equals("5")) {
                running = false;
                System.out.println("Goodbye!");
                break;
            }

            if (!choice.matches("[1-4]")) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            double num1 = readNumber(scanner, "Enter first number: ");
            double num2 = readNumber(scanner, "Enter second number: ");

            double result;
            switch (choice) {
                case "1":
                    result = num1 + num2;
                    System.out.printf("Result: %s + %s = %s%n", num1, num2, result);
                    break;
                case "2":
                    result = num1 - num2;
                    System.out.printf("Result: %s - %s = %s%n", num1, num2, result);
                    break;
                case "3":
                    result = num1 * num2;
                    System.out.printf("Result: %s * %s = %s%n", num1, num2, result);
                    break;
                case "4":
                    if (num2 == 0) {
                        System.out.println("Error: Cannot divide by zero.");
                    } else {
                        result = num1 / num2;
                        System.out.printf("Result: %s / %s = %s%n", num1, num2, result);
                    }
                    break;
                default:
                    System.out.println("Unknown operation.");
            }
        }

        scanner.close();
    }

    /**
     * Reads a valid number from the user, re-prompting on invalid input.
     */
    private static double readNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid numeric value.");
            }
        }
    }
}
