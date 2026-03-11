package atm.util;

import java.sql.Connection;

import atm.db.DBConnection;

public class ConnectionTest {
    public static void testConnection() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Database connected successfully.");
            } else {
                System.out.println("Database connection failed.");
            }
        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }
}