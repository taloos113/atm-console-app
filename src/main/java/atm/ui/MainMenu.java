package atm.ui;

import atm.model.Account;
import atm.repository.AccountRepository;
import atm.repository.IAccountRepository;
import atm.repository.ITransactionRepository;
import atm.repository.TransactionRepository;
import atm.service.AdminService;
import atm.service.AuthService;
import atm.service.CustomerService;
import atm.util.ConnectionTest;
import java.util.Scanner;

/**
 * Console entry menu for login and application exit.
 */
public class MainMenu {
    private final Scanner scanner;
    private final AuthService authService;
    private final AdminService adminService;
    private final CustomerService customerService;

    /**
     * Creates a main menu and wires concrete repository implementations into services.
     */
    public MainMenu() {
        IAccountRepository accountRepository = new AccountRepository();
        ITransactionRepository transactionRepository = new TransactionRepository();
        this.scanner = new Scanner(System.in);
        this.authService = new AuthService(accountRepository);
        this.adminService = new AdminService(accountRepository);
        this.customerService = new CustomerService(accountRepository, transactionRepository);
    }

    /**
     * Starts the main application loop.
     */
    public void start() {
        System.out.println("=================================");
        System.out.println(" Welcome to ATM System ");
        System.out.println("=================================");
        ConnectionTest.testConnection();

        boolean running = true;
        while (running) {
            System.out.println("\nMain Menu");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Please choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> {
                    System.out.println("Exiting system. Goodbye.");
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleLogin() {
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter 5-digit PIN: ");
        String pin = scanner.nextLine();

        Account account = authService.login(login, pin);
        if (account == null) {
            System.out.println("Invalid login credentials.");
            return;
        }

        System.out.println("Login successful. Role: " + account.getRole());
        if ("CUSTOMER".equalsIgnoreCase(account.getRole())) {
            new CustomerMenu(customerService).start(account);
        } else if ("ADMIN".equalsIgnoreCase(account.getRole())) {
            new AdminMenu(adminService).start(account);
        } else {
            System.out.println("Unknown role. Access denied.");
        }
    }
}
