/**
 * PalindromeChecker.java
 * Checks whether a given string or number is a palindrome.
 * Demonstrates string manipulation, recursion, and number operations.
 */
public class PalindromeChecker {

    // Check if a String is a palindrome (ignores case and spaces)
    public static boolean isStringPalindrome(String str) {
        if (str == null) return false;
        // Remove non-alphanumeric characters and convert to lowercase
        String cleaned = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        int left = 0, right = cleaned.length() - 1;
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }

    // Check palindrome using recursion
    public static boolean isRecursivePalindrome(String str, int left, int right) {
        if (left >= right) return true;
        if (str.charAt(left) != str.charAt(right)) return false;
        return isRecursivePalindrome(str, left + 1, right - 1);
    }

    // Check if a number is a palindrome
    public static boolean isNumberPalindrome(int num) {
        if (num < 0) return false;
        if (num < 10) return true;

        int original = num;
        int reversed = 0;
        while (num > 0) {
            reversed = reversed * 10 + num % 10;
            num /= 10;
        }
        return original == reversed;
    }

    // Reverse a string
    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    // Find all palindromic words in a sentence
    public static void findPalindromes(String sentence) {
        System.out.println("\nFinding palindromes in: \"" + sentence + "\"");
        String[] words = sentence.split("\\s+");
        boolean found = false;
        for (String word : words) {
            String cleaned = word.replaceAll("[^a-zA-Z0-9]", "");
            if (!cleaned.isEmpty() && isStringPalindrome(cleaned)) {
                System.out.println("  ✔ Palindrome found: \"" + cleaned + "\"");
                found = true;
            }
        }
        if (!found) System.out.println("  No palindromic words found.");
    }

    // Count palindromes in a range
    public static int countPalindromesInRange(int start, int end) {
        int count = 0;
        for (int i = start; i <= end; i++) {
            if (isNumberPalindrome(i)) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("        PALINDROME CHECKER PROGRAM      ");
        System.out.println("========================================\n");

        // Test string palindromes
        System.out.println("--- String Palindrome Checks ---");
        String[] testStrings = {
            "racecar", "hello", "madam", "Java",
            "A man a plan a canal Panama",
            "Was it a car or a cat I saw",
            "Never odd or even", "Not a palindrome"
        };

        for (String s : testStrings) {
            boolean result = isStringPalindrome(s);
            System.out.printf("  %-40s -> %s%n", "\"" + s + "\"",
                result ? "✔ Palindrome" : "✘ Not Palindrome");
        }

        // Test recursive palindrome
        System.out.println("\n--- Recursive Palindrome Check ---");
        String recTest = "level";
        boolean recResult = isRecursivePalindrome(recTest, 0, recTest.length() - 1);
        System.out.println("  \"" + recTest + "\" is" + (recResult ? "" : " NOT") + " a palindrome (recursive)");

        // Test number palindromes
        System.out.println("\n--- Number Palindrome Checks ---");
        int[] testNumbers = {121, 123, 1331, 12321, 10001, 99, 100, 1221};
        for (int num : testNumbers) {
            System.out.println("  " + num + " -> " + (isNumberPalindrome(num) ? "✔ Palindrome" : "✘ Not Palindrome"));
        }

        // Find palindromes in a sentence
        findPalindromes("madam went to the civic level racecar market");
        findPalindromes("hello world how are you");

        // Count palindromes in range
        int start = 1, end = 200;
        int count = countPalindromesInRange(start, end);
        System.out.println("\nCount of palindrome numbers from " + start + " to " + end + ": " + count);

        // String reversal
        System.out.println("\n--- String Reversal ---");
        String toReverse = "JavaProgramming";
        System.out.println("  Original : " + toReverse);
        System.out.println("  Reversed : " + reverseString(toReverse));

        System.out.println("\n========================================");
        System.out.println("           Program Completed!           ");
        System.out.println("========================================");
    }
}
