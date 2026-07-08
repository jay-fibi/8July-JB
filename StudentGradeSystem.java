/**
 * StudentGradeSystem.java
 * A simple Student Grade Management System.
 * Demonstrates OOP concepts: classes, objects, arrays, and methods.
 */
public class StudentGradeSystem {

    // Inner class representing a Student
    static class Student {
        String name;
        int rollNumber;
        double[] marks;
        String[] subjects;

        Student(String name, int rollNumber, String[] subjects, double[] marks) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.subjects = subjects;
            this.marks = marks;
        }

        // Calculate total marks
        double getTotalMarks() {
            double total = 0;
            for (double mark : marks) total += mark;
            return total;
        }

        // Calculate average percentage
        double getPercentage() {
            return (getTotalMarks() / (marks.length * 100)) * 100;
        }

        // Get Grade based on percentage
        String getGrade() {
            double percentage = getPercentage();
            if (percentage >= 90) return "A+";
            else if (percentage >= 80) return "A";
            else if (percentage >= 70) return "B";
            else if (percentage >= 60) return "C";
            else if (percentage >= 50) return "D";
            else return "F";
        }

        // Check if student passed (min 35 in each subject)
        boolean hasPassed() {
            for (double mark : marks) {
                if (mark < 35) return false;
            }
            return true;
        }

        // Get highest scored subject
        String getHighestSubject() {
            int maxIdx = 0;
            for (int i = 1; i < marks.length; i++) {
                if (marks[i] > marks[maxIdx]) maxIdx = i;
            }
            return subjects[maxIdx] + " (" + marks[maxIdx] + ")";
        }

        // Print student report card
        void printReportCard() {
            System.out.println("\n  +------------------------------------------+");
            System.out.printf("  | %-40s |%n", "REPORT CARD");
            System.out.println("  +------------------------------------------+");
            System.out.printf("  | Name       : %-27s |%n", name);
            System.out.printf("  | Roll No    : %-27d |%n", rollNumber);
            System.out.println("  +------------------------------------------+");
            System.out.printf("  | %-20s | %-17s |%n", "Subject", "Marks (out of 100)");
            System.out.println("  +----------------------+-------------------+");
            for (int i = 0; i < subjects.length; i++) {
                System.out.printf("  | %-20s | %-17.1f |%n", subjects[i], marks[i]);
            }
            System.out.println("  +----------------------+-------------------+");
            System.out.printf("  | Total Marks: %-27.1f |%n", getTotalMarks());
            System.out.printf("  | Percentage : %-26.2f%% |%n", getPercentage());
            System.out.printf("  | Grade      : %-27s |%n", getGrade());
            System.out.printf("  | Status     : %-27s |%n", hasPassed() ? "PASS ✔" : "FAIL ✘");
            System.out.printf("  | Best Subject: %-26s |%n", getHighestSubject());
            System.out.println("  +------------------------------------------+");
        }
    }

    // Find topper among students
    static Student findTopper(Student[] students) {
        Student topper = students[0];
        for (Student s : students) {
            if (s.getPercentage() > topper.getPercentage()) topper = s;
        }
        return topper;
    }

    // Calculate class average
    static double classAverage(Student[] students) {
        double total = 0;
        for (Student s : students) total += s.getPercentage();
        return total / students.length;
    }

    // Count passed students
    static int countPassed(Student[] students) {
        int count = 0;
        for (Student s : students) if (s.hasPassed()) count++;
        return count;
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("       STUDENT GRADE MANAGEMENT SYSTEM    ");
        System.out.println("==========================================");

        String[] subjects = {"Mathematics", "Science", "English", "History", "Computer"};

        // Create students
        Student[] students = {
            new Student("Alice Johnson", 101, subjects, new double[]{92, 88, 95, 78, 99}),
            new Student("Bob Smith",     102, subjects, new double[]{75, 65, 80, 70, 85}),
            new Student("Charlie Brown", 103, subjects, new double[]{55, 60, 58, 48, 62}),
            new Student("Diana Prince",  104, subjects, new double[]{98, 95, 97, 92, 100}),
            new Student("Eve Wilson",    105, subjects, new double[]{30, 40, 35, 25, 45})
        };

        // Print each student's report card
        for (Student s : students) {
            s.printReportCard();
        }

        // Class summary
        System.out.println("\n==========================================");
        System.out.println("            CLASS SUMMARY                ");
        System.out.println("==========================================");
        System.out.printf("  Total Students  : %d%n", students.length);
        System.out.printf("  Passed Students : %d%n", countPassed(students));
        System.out.printf("  Failed Students : %d%n", students.length - countPassed(students));
        System.out.printf("  Class Average   : %.2f%%%n", classAverage(students));
        System.out.println("  Class Topper    : " + findTopper(students).name +
                           " (" + String.format("%.2f", findTopper(students).getPercentage()) + "%)");

        System.out.println("\n==========================================");
        System.out.println("            Program Completed!            ");
        System.out.println("==========================================");
    }
}
