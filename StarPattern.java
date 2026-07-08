import java.util.Scanner;

/**
 * A simple console-based program that prints various star (*) patterns.
 * Supported patterns: right triangle, inverted triangle, pyramid,
 * and a square.
 */
public class StarPattern {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Java Star Pattern Printer ===");

        while (running) {
            System.out.println();
            System.out.println("Choose a pattern:");
            System.out.println("  1. Right Triangle");
            System.out.println("  2. Inverted Triangle");
            System.out.println("  3. Pyramid");
            System.out.println("  4. Square");
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

            int rows = readPositiveInt(scanner, "Enter number of rows: ");
            System.out.println();

            switch (choice) {
                case "1":
                    printRightTriangle(rows);
                    break;
                case "2":
                    printInvertedTriangle(rows);
                    break;
                case "3":
                    printPyramid(rows);
                    break;
                case "4":
                    printSquare(rows);
                    break;
                default:
                    System.out.println("Unknown pattern.");
            }
        }

        scanner.close();
    }

    /** Prints a left-aligned right triangle of the given height. */
    public static void printRightTriangle(int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    /** Prints an inverted (top-heavy) right triangle of the given height. */
    public static void printInvertedTriangle(int rows) {
        for (int i = rows; i >= 1; i--) {
            for (int j = 1; j <= i; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    /** Prints a centered pyramid of the given height. */
    public static void printPyramid(int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int space = 1; space <= rows - i; space++) {
                System.out.print(" ");
            }
            for (int star = 1; star <= (2 * i - 1); star++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }

    /** Prints a solid square of the given side length. */
    public static void printSquare(int side) {
        for (int i = 1; i <= side; i++) {
            for (int j = 1; j <= side; j++) {
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    /**
     * Reads a positive integer from the user, re-prompting on invalid input.
     */
    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Please enter a positive whole number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a positive whole number.");
            }
        }
    }
}
