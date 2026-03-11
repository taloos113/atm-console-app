package atm.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

import atm.db.DBConnection;

public class TransactionRepository {

    public boolean addTransaction(int accountNumber, String transactionType,
                                  BigDecimal amount, BigDecimal balanceAfter, String note) {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, note) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountNumber);
            stmt.setString(2, transactionType);
            stmt.setBigDecimal(3, amount);
            stmt.setBigDecimal(4, balanceAfter);
            stmt.setString(5, note);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error adding transaction: " + e.getMessage());
            return false;
        }
    }
}