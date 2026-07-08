import java.util.Scanner;

/**
 * A simple console-based calculator that supports basic arithmetic
 * (add, subtract, multiply, divide) plus power, modulus, square root,
 * and percentage operations.
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
            System.out.println("  5. Power (^)");
            System.out.println("  6. Modulus (%)");
            System.out.println("  7. Square Root (\u221a)");
            System.out.println("  8. Percentage (x% of y)");
            System.out.println("  9. Exit");
            System.out.print("Enter choice (1-9): ");

            String choice = scanner.next();

            if (choice.equals("9")) {
                running = false;
                System.out.println("Goodbye!");
                break;
            }

            if (!choice.matches("[1-8]")) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            // Square root only needs a single operand.
            if (choice.equals("7")) {
                double num = readNumber(scanner, "Enter number: ");
                if (num < 0) {
                    System.out.println("Error: Cannot take the square root of a negative number.");
                } else {
                    System.out.printf("Result: \u221a%s = %s%n", num, squareRoot(num));
                }
                continue;
            }

            double num1 = readNumber(scanner, "Enter first number: ");
            double num2 = readNumber(scanner, "Enter second number: ");

            switch (choice) {
                case "1":
                    System.out.printf("Result: %s + %s = %s%n", num1, num2, add(num1, num2));
                    break;
                case "2":
                    System.out.printf("Result: %s - %s = %s%n", num1, num2, subtract(num1, num2));
                    break;
                case "3":
                    System.out.printf("Result: %s * %s = %s%n", num1, num2, multiply(num1, num2));
                    break;
                case "4":
                    if (num2 == 0) {
                        System.out.println("Error: Cannot divide by zero.");
                    } else {
                        System.out.printf("Result: %s / %s = %s%n", num1, num2, divide(num1, num2));
                    }
                    break;
                case "5":
                    System.out.printf("Result: %s ^ %s = %s%n", num1, num2, power(num1, num2));
                    break;
                case "6":
                    if (num2 == 0) {
                        System.out.println("Error: Cannot perform modulus by zero.");
                    } else {
                        System.out.printf("Result: %s %% %s = %s%n", num1, num2, modulus(num1, num2));
                    }
                    break;
                case "8":
                    System.out.printf("Result: %s%% of %s = %s%n", num1, num2, percentage(num1, num2));
                    break;
                default:
                    System.out.println("Unknown operation.");
            }
        }

        scanner.close();
    }

    /** Returns the sum of two numbers. */
    public static double add(double a, double b) {
        return a + b;
    }

    /** Returns the difference of two numbers. */
    public static double subtract(double a, double b) {
        return a - b;
    }

    /** Returns the product of two numbers. */
    public static double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Returns the quotient of two numbers.
     * @throws ArithmeticException if the divisor is zero.
     */
    public static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero.");
        }
        return a / b;
    }

    /** Returns {@code base} raised to the power of {@code exponent}. */
    public static double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Returns the remainder of {@code a} divided by {@code b}.
     * @throws ArithmeticException if the divisor is zero.
     */
    public static double modulus(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot perform modulus by zero.");
        }
        return a % b;
    }

    /**
     * Returns the square root of a number.
     * @throws IllegalArgumentException if the value is negative.
     */
    public static double squareRoot(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cannot take the square root of a negative number.");
        }
        return Math.sqrt(value);
    }

    /** Returns {@code percent} percent of {@code value} (e.g. 20% of 50 = 10). */
    public static double percentage(double percent, double value) {
        return (percent / 100.0) * value;
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

