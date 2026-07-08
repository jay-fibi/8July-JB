/**
 * FibonacciSeries.java
 * Generates Fibonacci series up to a given number of terms.
 * Demonstrates loops, arrays, and basic math operations in Java.
 */
public class FibonacciSeries {

    // Iterative method to generate Fibonacci series
    public static long[] generateFibonacci(int n) {
        if (n <= 0) return new long[0];
        if (n == 1) return new long[]{0};

        long[] series = new long[n];
        series[0] = 0;
        series[1] = 1;

        for (int i = 2; i < n; i++) {
            series[i] = series[i - 1] + series[i - 2];
        }
        return series;
    }

    // Recursive method to get the Nth Fibonacci number
    public static long fibonacciRecursive(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    // Check if a number belongs to Fibonacci series
    public static boolean isFibonacci(long num) {
        long a = 0, b = 1;
        while (b < num) {
            long temp = b;
            b = a + b;
            a = temp;
        }
        return b == num || num == 0;
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("        FIBONACCI SERIES PROGRAM        ");
        System.out.println("========================================\n");

        // Generate first 15 Fibonacci numbers
        int terms = 15;
        long[] series = generateFibonacci(terms);

        System.out.println("First " + terms + " Fibonacci Numbers (Iterative):");
        System.out.print("[ ");
        for (int i = 0; i < series.length; i++) {
            System.out.print(series[i]);
            if (i < series.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");

        // Demonstrate recursive approach for small values
        System.out.println("\nFibonacci Numbers using Recursion (1st to 10th term):");
        for (int i = 1; i <= 10; i++) {
            System.out.println("  Fibonacci(" + i + ") = " + fibonacciRecursive(i));
        }

        // Check if numbers belong to Fibonacci series
        System.out.println("\nChecking if numbers are part of Fibonacci series:");
        long[] testNumbers = {0, 1, 5, 7, 13, 20, 21, 34, 50, 55};
        for (long num : testNumbers) {
            System.out.println("  " + num + " -> " + (isFibonacci(num) ? "✔ Is Fibonacci" : "✘ Not Fibonacci"));
        }

        // Sum of first N Fibonacci numbers
        long sum = 0;
        for (long num : series) sum += num;
        System.out.println("\nSum of first " + terms + " Fibonacci numbers = " + sum);

        System.out.println("\n========================================");
        System.out.println("           Program Completed!           ");
        System.out.println("========================================");
    }
}
