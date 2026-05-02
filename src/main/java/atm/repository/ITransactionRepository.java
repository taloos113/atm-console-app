package atm.repository;

import java.math.BigDecimal;

/**
 * Repository contract for transaction persistence operations.
 */
public interface ITransactionRepository {
    boolean addTransaction(int accountNumber, String transactionType, BigDecimal amount,
                           BigDecimal balanceAfter, String note);
}
