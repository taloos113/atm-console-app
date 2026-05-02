package atm.ui;

import atm.model.Account;
import atm.service.AdminService;
import atm.util.InputUtil;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console menu for administrator account management operations.
 */
public class AdminMenu {
    private final Scanner scanner;
    private final AdminService adminService;

    /**
     * Creates an admin menu with its service dependency.
     *
     * @param adminService admin service dependency
     */
    public AdminMenu(AdminService adminService) {
        this.scanner = new Scanner(System.in);
        this.adminService = adminService;
    }

    /**
     * Starts the administrator menu loop.
     *
     * @param account authenticated admin account
     */
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
                case "1" -> handleCreateAccount();
                case "2" -> handleDeleteAccount();
                case "3" -> handleUpdateAccount();
                case "4" -> handleSearchAccount();
                case "5" -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleCreateAccount() {
        try {
            String login = InputUtil.readNonEmptyString(scanner, "Enter login: ");
            String pin = InputUtil.readPin(scanner, "Enter 5-digit PIN: ");
            String holderName = InputUtil.readNonEmptyString(scanner, "Enter holder name: ");
            BigDecimal balance = InputUtil.readBigDecimal(scanner, "Enter initial balance: ");
            String status = InputUtil.readStatus(scanner, "Enter status (ACTIVE/DISABLED): ");
            String role = InputUtil.readRole(scanner, "Enter role (CUSTOMER/ADMIN): ");

            Optional<Account> created = adminService.createAccount(login, pin, holderName, balance, status, role);
            if (created.isPresent()) {
                System.out.println("Account created successfully. Assigned account number: "
                        + created.get().getAccountNumber());
            } else {
                System.out.println("Failed to create account.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void handleDeleteAccount() {
        try {
            int accountNumber = InputUtil.readInt(scanner, "Enter account number to delete: ");
            Optional<Account> found = adminService.searchAccount(accountNumber);
            if (found.isEmpty()) {
                System.out.println("Account not found.");
                return;
            }

            System.out.println("You wish to delete the account held by " + found.get().getHolderName() + ".");
            int confirmation = InputUtil.readInt(scanner, "Please re-enter the account number to confirm: ");
            if (confirmation != accountNumber) {
                System.out.println("Delete operation cancelled.");
                return;
            }

            boolean success = adminService.deleteAccount(accountNumber);
            System.out.println(success ? "Account deleted successfully." : "Failed to delete account.");
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void handleUpdateAccount() {
        try {
            int accountNumber = InputUtil.readInt(scanner, "Enter account number to update: ");
            String login = InputUtil.readNonEmptyString(scanner, "Enter new login: ");
            String pin = InputUtil.readPin(scanner, "Enter new 5-digit PIN: ");
            String holderName = InputUtil.readNonEmptyString(scanner, "Enter new holder name: ");
            String status = InputUtil.readStatus(scanner, "Enter new status (ACTIVE/DISABLED): ");
            String role = InputUtil.readRole(scanner, "Enter new role (CUSTOMER/ADMIN): ");

            boolean success = adminService.updateAccount(accountNumber, login, pin, holderName, status, role);
            System.out.println(success ? "Account updated successfully." : "Failed to update account.");
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void handleSearchAccount() {
        try {
            int accountNumber = InputUtil.readInt(scanner, "Enter account number to search: ");
            Optional<Account> found = adminService.searchAccount(accountNumber);
            if (found.isEmpty()) {
                System.out.println("Account not found.");
                return;
            }
            printAccountInformation(found.get());
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void printAccountInformation(Account account) {
        System.out.println("\n========== Account Information ==========");
        System.out.println("Account Number : " + account.getAccountNumber());
        System.out.println("Login : " + account.getLogin());
        System.out.println("PIN Code : " + account.getPinCode());
        System.out.println("Holder Name : " + account.getHolderName());
        System.out.println("Balance : $" + account.getBalance());
        System.out.println("Status : " + account.getStatus());
        System.out.println("Role : " + account.getRole());
        System.out.println("=========================================\n");
    }
}
