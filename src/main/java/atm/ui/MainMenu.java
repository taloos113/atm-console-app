package atm.ui;

import java.util.Scanner;

import atm.model.Account;
import atm.service.AuthService;
import atm.util.ConnectionTest;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService = new AuthService();

    public void start() {
        System.out.println("=================================");
        System.out.println("     Welcome to ATM System       ");
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
                case "1":
                    handleLogin();
                    break;
                case "2":
                    System.out.println("Exiting system. Goodbye.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
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
            new CustomerMenu().start(account);
        } else if ("ADMIN".equalsIgnoreCase(account.getRole())) {
            new AdminMenu().start(account);
        } else {
            System.out.println("Unknown role. Access denied.");
        }
    }
}