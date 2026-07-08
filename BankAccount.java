/**
 * BankAccount.java
 * Bank Account Management System.
 * Demonstrates OOP: encapsulation, constructors, methods, and transaction history.
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BankAccount {

    static class Transaction {
        String type;
        double amount;
        double balanceAfter;
        String timestamp;

        Transaction(String type, double amount, double balanceAfter) {
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        public String toString() {
            return String.format("[%s] %-12s Amount:%8.2f Balance:%10.2f",
                timestamp, type, amount, balanceAfter);
        }
    }

    private String accountHolder;
    private String accountNumber;
    private String accountType;
    private double balance;
    private List<Transaction> transactions;
    private static final double MIN_BALANCE = 500.0;

    public BankAccount(String holder, String number, String type, double initial) {
        this.accountHolder = holder;
        this.accountNumber = number;
        this.accountType = type;
        this.balance = 0;
        this.transactions = new ArrayList<>();
        deposit(initial);
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  [ERROR] Deposit amount must be positive.");
            return false;
        }
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, balance));
        System.out.printf("  [OK] Deposited: %.2f | Balance: %.2f%n", amount, balance);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  [ERROR] Withdrawal amount must be positive.");
            return false;
        }
        if (balance - amount < MIN_BALANCE) {
            System.out.printf("  [ERROR] Min balance %.2f required. Withdrawal denied.%n", MIN_BALANCE);
            return false;
        }
        balance -= amount;
        transactions.add(new Transaction("WITHDRAW", amount, balance));
        System.out.printf("  [OK] Withdrawn: %.2f | Balance: %.2f%n", amount, balance);
        return true;
    }

    public boolean transfer(BankAccount target, double amount) {
        System.out.printf("  [INFO] Transferring %.2f to %s...%n", amount, target.accountHolder);
        if (withdraw(amount)) {
            target.balance += amount;
            target.transactions.add(new Transaction("TRANSFER IN", amount, target.balance));
            System.out.printf("  [OK] Transfer done. Target balance: %.2f%n", target.balance);
            return true;
        }
        System.out.println("  [ERROR] Transfer failed.");
        return false;
    }

    public void applyInterest(double rate) {
        if (!accountType.equalsIgnoreCase("Savings")) {
            System.out.println("  [ERROR] Interest only for Savings accounts.");
            return;
        }
        double interest = balance * (rate / 100);
        balance += interest;
        transactions.add(new Transaction("INTEREST", interest, balance));
        System.out.printf("  [OK] Interest: %.2f (%.1f%%) | Balance: %.2f%n", interest, rate, balance);
    }

    public void printSummary() {
        System.out.println("\n  --- Account Summary ---");
        System.out.printf("  Holder  : %s%n", accountHolder);
        System.out.printf("  Number  : %s%n", accountNumber);
        System.out.printf("  Type    : %s%n", accountType);
        System.out.printf("  Balance : %.2f%n", balance);
        System.out.printf("  Transactions: %d%n", transactions.size());
    }

    public void printTransactionHistory() {
        System.out.println("\n  --- Transaction History: " + accountHolder + " ---");
        if (transactions.isEmpty()) {
            System.out.println("  No transactions.");
            return;
        }
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + transactions.get(i));
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      BANK ACCOUNT MANAGEMENT SYSTEM     ");
        System.out.println("==========================================");

        BankAccount alice = new BankAccount("Alice Johnson", "ACC-1001", "Savings", 10000);
        BankAccount bob   = new BankAccount("Bob Smith",     "ACC-1002", "Current", 5000);

        System.out.println("\n--- Alice's Transactions ---");
        alice.deposit(5000);
        alice.withdraw(3000);
        alice.withdraw(20000);
        alice.applyInterest(4.5);

        System.out.println("\n--- Bob's Transactions ---");
        bob.deposit(2000);
        bob.withdraw(1500);
        bob.applyInterest(3.0);

        System.out.println("\n--- Transfer: Alice -> Bob ---");
        alice.transfer(bob, 2000);

        alice.printSummary();
        bob.printSummary();
        alice.printTransactionHistory();
        bob.printTransactionHistory();

        System.out.println("\n==========================================");
        System.out.println("            Program Completed!            ");
        System.out.println("==========================================");
    }
}
