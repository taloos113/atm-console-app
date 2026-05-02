package atm.util;

import atm.db.DBConnection;
import java.sql.Connection;

/**
 * Simple utility for checking whether the database connection can be opened.
 */
public final class ConnectionTest {
    private ConnectionTest() {
    }

    /**
     * Prints the database connection status.
     */
    public static void testConnection() {
        try (Connection connection = DBConnection.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful.");
            }
        } catch (Exception exception) {
            System.out.println("Database connection failed: " + exception.getMessage());
        }
    }
}
