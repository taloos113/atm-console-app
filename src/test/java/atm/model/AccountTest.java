package atm.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

class AccountTest {
    @Test
    void constructorStoresAllFields() {
        Account account = sampleAccount();

        assertEquals(1, account.getAccountNumber());
        assertEquals("alice", account.getLogin());
        assertEquals("11111", account.getPinCode());
        assertEquals("Alice Johnson", account.getHolderName());
        assertEquals(BigDecimal.valueOf(1200), account.getBalance());
        assertEquals("ACTIVE", account.getStatus());
        assertEquals("CUSTOMER", account.getRole());
    }

    @Test
    void constructorRejectsNullLogin() {
        assertThrows(NullPointerException.class, () -> new Account(
                1, null, "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER"));
    }

    @Test
    void constructorRejectsNullPinCode() {
        assertThrows(NullPointerException.class, () -> new Account(
                1, "alice", null, "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER"));
    }

    @Test
    void constructorRejectsNullHolderName() {
        assertThrows(NullPointerException.class, () -> new Account(
                1, "alice", "11111", null,
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER"));
    }

    @Test
    void constructorRejectsNullBalance() {
        assertThrows(NullPointerException.class, () -> new Account(
                1, "alice", "11111", "Alice Johnson",
                null, "ACTIVE", "CUSTOMER"));
    }

    @Test
    void constructorRejectsNullStatus() {
        assertThrows(NullPointerException.class, () -> new Account(
                1, "alice", "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), null, "CUSTOMER"));
    }

    @Test
    void constructorRejectsNullRole() {
        assertThrows(NullPointerException.class, () -> new Account(
                1, "alice", "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", null));
    }

    @Test
    void withAccountNumberCreatesUpdatedCopy() {
        Account original = sampleAccount();

        Account updated = original.withAccountNumber(99);

        assertEquals(1, original.getAccountNumber());
        assertEquals(99, updated.getAccountNumber());
        assertEquals(original.getLogin(), updated.getLogin());
        assertEquals(original.getBalance(), updated.getBalance());
    }

    @Test
    void withBalanceCreatesNewAccountWithoutMutatingOriginal() {
        Account original = sampleAccount();

        Account updated = original.withBalance(BigDecimal.valueOf(1400));

        assertEquals(BigDecimal.valueOf(1200), original.getBalance());
        assertEquals(BigDecimal.valueOf(1400), updated.getBalance());
    }

    @Test
    void withProfileCreatesUpdatedProfileCopy() {
        Account original = sampleAccount();

        Account updated = original.withProfile("alice2", "22222", "Alice B", "DISABLED", "CUSTOMER");

        assertEquals("alice", original.getLogin());
        assertEquals("alice2", updated.getLogin());
        assertEquals("22222", updated.getPinCode());
        assertEquals("Alice B", updated.getHolderName());
        assertEquals("DISABLED", updated.getStatus());
        assertEquals("CUSTOMER", updated.getRole());
    }

    @Test
    void fromResultSetMapsAllFields() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getInt("account_number")).thenReturn(7);
        when(resultSet.getString("login")).thenReturn("bob");
        when(resultSet.getString("pin_code")).thenReturn("22222");
        when(resultSet.getString("holder_name")).thenReturn("Bob Smith");
        when(resultSet.getBigDecimal("balance")).thenReturn(BigDecimal.valueOf(850));
        when(resultSet.getString("status")).thenReturn("ACTIVE");
        when(resultSet.getString("role")).thenReturn("CUSTOMER");

        Account account = Account.fromResultSet(resultSet);

        assertEquals(7, account.getAccountNumber());
        assertEquals("bob", account.getLogin());
        assertEquals("22222", account.getPinCode());
        assertEquals("Bob Smith", account.getHolderName());
        assertEquals(BigDecimal.valueOf(850), account.getBalance());
        assertEquals("ACTIVE", account.getStatus());
        assertEquals("CUSTOMER", account.getRole());
    }

    private Account sampleAccount() {
        return new Account(1, "alice", "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER");
    }
}