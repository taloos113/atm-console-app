package atm.ui;

import java.math.BigDecimal;
import java.util.Scanner;

import atm.model.Account;
import atm.service.AdminService;
import atm.util.InputUtil;

public class AdminMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminService adminService = new AdminService();

    public void start(Account account) {
        boolean running = true;

        while (running) {
            System.out.println("\n=================================");
            System.out.println("Administrator Menu");
            System.out.println("Welcome, " + account.getHolderName());
            System.out.println("1. Create New Account");
            System.out.println("2. Delete Existing Account");
            System.out.println("3. Update Account Information");
            System.out.println("4. Search for Account");
            System.out.println("5. Logout");
            System.out.print("Please choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleCreateAccount();
                    break;
                case "2":
                    handleDeleteAccount();
                    break;
                case "3":
                    handleUpdateAccount();
                    break;
                case "4":
                    handleSearchAccount();
                    break;
                case "5":
                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleCreateAccount() {
        String login = InputUtil.readNonEmptyString(scanner, "Enter login: ");
        String pin = InputUtil.readPin(scanner, "Enter 5-digit PIN: ");
        String holderName = InputUtil.readNonEmptyString(scanner, "Enter holder name: ");
        BigDecimal balance = InputUtil.readBigDecimal(scanner, "Enter initial balance: ");
        String status = InputUtil.readStatus(scanner, "Enter status (ACTIVE/DISABLED): ");
        String role = InputUtil.readRole(scanner, "Enter role (CUSTOMER/ADMIN): ");

        boolean success = adminService.createAccount(login, pin, holderName, balance, status, role);

        if (success) {
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Failed to create account.");
        }
    }

    private void handleDeleteAccount() {
        int accountNumber = InputUtil.readInt(scanner, "Enter account number to delete: ");

        System.out.print("Are you sure you want to delete this account? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!"Y".equalsIgnoreCase(confirm)) {
            System.out.println("Delete operation cancelled.");
            return;
        }

        boolean success = adminService.deleteAccount(accountNumber);

        if (success) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Failed to delete account.");
        }
    }

    private void handleUpdateAccount() {
        int accountNumber = InputUtil.readInt(scanner, "Enter account number to update: ");
        String login = InputUtil.readNonEmptyString(scanner, "Enter new login: ");
        String pin = InputUtil.readPin(scanner, "Enter new 5-digit PIN: ");
        String holderName = InputUtil.readNonEmptyString(scanner, "Enter new holder name: ");
        String status = InputUtil.readStatus(scanner, "Enter new status (ACTIVE/DISABLED): ");
        String role = InputUtil.readRole(scanner, "Enter new role (CUSTOMER/ADMIN): ");

        boolean success = adminService.updateAccount(accountNumber, login, pin, holderName, status, role);

        if (success) {
            System.out.println("Account updated successfully.");
        } else {
            System.out.println("Failed to update account.");
        }
    }

    private void handleSearchAccount() {
        int accountNumber = InputUtil.readInt(scanner, "Enter account number to search: ");

        Account found = adminService.searchAccount(accountNumber);

        if (found == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.println("\n========== Account Information ==========");
        System.out.println("Account Number : " + found.getAccountNumber());
        System.out.println("Login          : " + found.getLogin());
        System.out.println("Holder Name    : " + found.getHolderName());
        System.out.println("Balance        : $" + found.getBalance());
        System.out.println("Status         : " + found.getStatus());
        System.out.println("Role           : " + found.getRole());
        System.out.println("=========================================\n");
    }
}