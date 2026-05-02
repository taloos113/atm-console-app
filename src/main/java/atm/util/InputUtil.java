package atm.util;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Shared console input validation helpers.
 */
public final class InputUtil {
    private InputUtil() {
    }

    /**
     * Reads a valid integer from the console.
     *
     * @param scanner scanner used for input
     * @param prompt prompt shown to the user
     * @return parsed integer
     */
    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception exception) {
                System.out.println("Invalid integer. Please try again.");
            }
        }
    }

    /**
     * Reads a valid decimal number from the console.
     *
     * @param scanner scanner used for input
     * @param prompt prompt shown to the user
     * @return parsed BigDecimal value
     */
    public static BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (Exception exception) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Reads a non-empty string from the console.
     *
     * @param scanner scanner used for input
     * @param prompt prompt shown to the user
     * @return non-empty string
     */
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

    /**
     * Reads a five digit PIN from the console.
     *
     * @param scanner scanner used for input
     * @param prompt prompt shown to the user
     * @return valid PIN
     */
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

    /**
     * Reads an account status from the console.
     *
     * @param scanner scanner used for input
     * @param prompt prompt shown to the user
     * @return ACTIVE or DISABLED
     */
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

    /**
     * Reads an account role from the console.
     *
     * @param scanner scanner used for input
     * @param prompt prompt shown to the user
     * @return CUSTOMER or ADMIN
     */
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
