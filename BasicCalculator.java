import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BasicCalculator {
    private static final String HISTORY_FILE = "history.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalculation = true;

        while (continueCalculation) {
            try {
                System.out.println("\nSelect an operation:");
                System.out.println("1. Addition (+)");
                System.out.println("2. Subtraction (-)");
                System.out.println("3. Multiplication (*)");
                System.out.println("4. Division (/)");
                System.out.println("5. View History");
                System.out.println("6. Exit");

                int choice = scanner.nextInt();

                if (choice == 6) {
                    System.out.println("Exiting calculator...");
                    break;
                } else if (choice == 5) {
                    displayHistory();
                    continue;
                }

                System.out.print("Enter first number: ");
                double num1 = scanner.nextDouble();

                System.out.print("Enter second number: ");
                double num2 = scanner.nextDouble();

                double result = 0;
                String operation = "";

                switch (choice) {
                    case 1:
                        result = add(num1, num2);
                        operation = num1 + " + " + num2 + " = " + result;
                        break;
                    case 2:
                        result = subtract(num1, num2);
                        operation = num1 + " - " + num2 + " = " + result;
                        break;
                    case 3:
                        result = multiply(num1, num2);
                        operation = num1 + " * " + num2 + " = " + result;
                        break;
                    case 4:
                        result = divide(num1, num2);
                        operation = num1 + " / " + num2 + " = " + result;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid operation.");
                        continue;
                }

                System.out.println("Result: " + result);
                saveToHistory(operation);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); 
            } catch (ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error saving to history: " + e.getMessage());
            }
        }
        scanner.close();
    }


    private static double add(double a, double b) {
        return a + b;
    }


    private static double subtract(double a, double b) {
        return a - b;
    }


    private static double multiply(double a, double b) {
        return a * b;
    }


    private static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }


    private static void saveToHistory(String operation) throws IOException {
        try (FileWriter writer = new FileWriter(HISTORY_FILE, true)) {
            writer.write(operation + "\n");
        }
    }

    private static void displayHistory() {
        System.out.println("\n--- Calculation History ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading history: " + e.getMessage());
        }
    }
}
