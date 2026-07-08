import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=============================");
        System.out.println("      Simple Calculator      ");
        System.out.println("=============================");

        while (true) {
            System.out.println("\nSelect an operation:");
            System.out.println("  1. Addition       (+)");
            System.out.println("  2. Subtraction    (-)");
            System.out.println("  3. Multiplication (*)");
            System.out.println("  4. Division       (/)")
            System.out.println("  5. Exit");
            System.out.print("\nEnter your choice (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                continue;
            }

            if (choice == 5) {
                System.out.println("Goodbye! Thanks for using Simple Calculator.");
                break;
            }

            if (choice < 1 || choice > 4) {
                System.out.println("Invalid choice! Please select between 1 and 5.");
                continue;
            }

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

            double result;
            String operator;

            switch (choice) {
                case 1:
                    result   = num1 + num2;
                    operator = "+";
                    break;
                case 2:
                    result   = num1 - num2;
                    operator = "-";
                    break;
                case 3:
                    result   = num1 * num2;
                    operator = "*";
                    break;
                case 4:
                    if (num2 == 0) {
                        System.out.println("Error: Division by zero is not allowed!");
                        continue;
                    }
                    result   = num1 / num2;
                    operator = "/";
                    break;
                default:
                    System.out.println("Unexpected error.");
                    continue;
            }

            // Format result: show as integer when there's no fractional part
            if (result == (long) result) {
                System.out.printf("%nResult: %.0f %s %.0f = %.0f%n", num1, operator, num2, result);
            } else {
                System.out.printf("%nResult: %s %s %s = %s%n",
                        formatNumber(num1), operator, formatNumber(num2), formatNumber(result));
            }
        }

        scanner.close();
    }

    // Helper: strip unnecessary trailing zeros
    private static String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
