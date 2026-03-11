package atm.util;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputUtil {

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid integer. Please try again.");
            }
        }
    }

    public static BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                BigDecimal value = new BigDecimal(scanner.nextLine().trim());
                return value;
            } catch (Exception e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    public static String readPin(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String pin = scanner.nextLine().trim();
            if (pin.matches("\\d{5}")) {
                return pin;
            }
            System.out.println("PIN must be exactly 5 digits.");
        }
    }

    public static String readStatus(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String status = scanner.nextLine().trim().toUpperCase();
            if ("ACTIVE".equals(status) || "DISABLED".equals(status)) {
                return status;
            }
            System.out.println("Status must be ACTIVE or DISABLED.");
        }
    }

    public static String readRole(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String role = scanner.nextLine().trim().toUpperCase();
            if ("CUSTOMER".equals(role) || "ADMIN".equals(role)) {
                return role;
            }
            System.out.println("Role must be CUSTOMER or ADMIN.");
        }
    }
}