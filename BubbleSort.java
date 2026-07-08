/**
 * BubbleSort.java
 * Implements Bubble Sort algorithm with step-by-step visualization.
 * Demonstrates sorting, array manipulation, and algorithm efficiency.
 */
public class BubbleSort {

    // Standard Bubble Sort
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Swap arr[j] and arr[j+1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Optimized Bubble Sort (stops early if already sorted)
    public static int optimizedBubbleSort(int[] arr) {
        int n = arr.length;
        int passes = 0;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            passes++;
            if (!swapped) break; // Already sorted
        }
        return passes;
    }

    // Print array helper
    public static void printArray(int[] arr, String label) {
        System.out.print(label + ": [ ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }

    // Bubble Sort with visualization (step-by-step)
    public static void bubbleSortVisualized(int[] arr) {
        int n = arr.length;
        System.out.println("\n--- Step-by-Step Bubble Sort ---");
        printArray(arr, "Initial Array");

        for (int i = 0; i < n - 1; i++) {
            System.out.println("\nPass " + (i + 1) + ":");
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    System.out.println("  Swapping " + arr[j] + " and " + arr[j + 1]);
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            printArray(arr, "  After Pass " + (i + 1));
            if (!swapped) {
                System.out.println("  No swaps! Array is sorted early.");
                break;
            }
        }
    }

    // Sort strings using Bubble Sort
    public static void bubbleSortStrings(String[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].compareToIgnoreCase(arr[j + 1]) > 0) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("         BUBBLE SORT PROGRAM            ");
        System.out.println("========================================\n");

        // Test 1: Standard sort
        int[] arr1 = {64, 34, 25, 12, 22, 11, 90};
        printArray(arr1, "Original Array");
        bubbleSort(arr1);
        printArray(arr1, "Sorted Array  ");

        // Test 2: Optimized sort with pass count
        int[] arr2 = {5, 1, 4, 2, 8, 7, 3, 6};
        System.out.println("\n--- Optimized Bubble Sort ---");
        printArray(arr2, "Original Array");
        int passes = optimizedBubbleSort(arr2);
        printArray(arr2, "Sorted Array  ");
        System.out.println("Total passes taken: " + passes);

        // Test 3: Already sorted array
        int[] arr3 = {1, 2, 3, 4, 5};
        System.out.println("\n--- Already Sorted Array ---");
        printArray(arr3, "Original Array");
        int passes2 = optimizedBubbleSort(arr3);
        printArray(arr3, "Sorted Array  ");
        System.out.println("Total passes taken: " + passes2 + " (optimized early stop!)");

        // Test 4: Visualized sort
        int[] arr4 = {40, 10, 30, 20, 50};
        bubbleSortVisualized(arr4);

        // Test 5: Sort strings
        String[] names = {"Jay", "Alice", "Charlie", "Bob", "Diana"};
        System.out.println("\n--- Sorting Names ---");
        System.out.print("Original: [ ");
        for (String name : names) System.out.print(name + " ");
        System.out.println("]");
        bubbleSortStrings(names);
        System.out.print("Sorted:   [ ");
        for (String name : names) System.out.print(name + " ");
        System.out.println("]");

        System.out.println("\n========================================");
        System.out.println("           Program Completed!           ");
        System.out.println("========================================");
    }
}
