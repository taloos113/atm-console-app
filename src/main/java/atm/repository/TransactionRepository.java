package atm.repository;

import atm.db.DBConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * JDBC implementation of transaction SQL operations.
 */
public class TransactionRepository implements ITransactionRepository {
    @Override
    public boolean addTransaction(int accountNumber, String transactionType, BigDecimal amount,
                                  BigDecimal balanceAfter, String note) {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, note) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountNumber);
            statement.setString(2, transactionType);
            statement.setBigDecimal(3, amount);
            statement.setBigDecimal(4, balanceAfter);
            statement.setString(5, note);
            return statement.executeUpdate() > 0;
        } catch (Exception exception) {
            System.out.println("Error adding transaction: " + exception.getMessage());
            return false;
        }
    }
}
