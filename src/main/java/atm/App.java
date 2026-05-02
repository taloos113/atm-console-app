package atm;

import atm.ui.MainMenu;

/**
 * Main entry point for the ATM console application.
 */
public final class App {
    private App() {
    }

    /**
     * Starts the ATM application and handles unexpected application-level errors.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            MainMenu mainMenu = new MainMenu();
            mainMenu.start();
        } catch (Exception exception) {
            System.out.println("An unexpected error occurred. Please try again later.");
            exception.printStackTrace();
        }
    }
}
