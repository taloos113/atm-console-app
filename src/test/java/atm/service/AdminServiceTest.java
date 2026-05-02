package atm.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import atm.model.Account;
import atm.repository.IAccountRepository;

class AdminServiceTest {
    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final AdminService adminService = new AdminService(accountRepository);

    @Test
    void createAccountReturnsGeneratedAccount() {
        Account generated = new Account(10, "newuser", "12345", "New User",
                BigDecimal.valueOf(500), "ACTIVE", "CUSTOMER");
        when(accountRepository.findByLogin("newuser")).thenReturn(Optional.empty());
        when(accountRepository.createAccount(any(Account.class))).thenReturn(Optional.of(generated));

        Optional<Account> result = adminService.createAccount(
                "newuser", "12345", "New User", BigDecimal.valueOf(500), "ACTIVE", "CUSTOMER");

        assertTrue(result.isPresent());
        assertEquals(10, result.get().getAccountNumber());
    }

    @Test
    void createAccountTrimsAndNormalizesFields() {
        Account generated = new Account(11, "newuser", "12345", "New User",
                BigDecimal.valueOf(500), "ACTIVE", "CUSTOMER");
        when(accountRepository.findByLogin("newuser")).thenReturn(Optional.empty());
        when(accountRepository.createAccount(any(Account.class))).thenReturn(Optional.of(generated));

        adminService.createAccount(
                " newuser ", " 12345 ", " New User ", BigDecimal.valueOf(500), " active ", " customer ");

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).createAccount(captor.capture());

        Account created = captor.getValue();
        assertEquals("newuser", created.getLogin());
        assertEquals("12345", created.getPinCode());
        assertEquals("New User", created.getHolderName());
        assertEquals("ACTIVE", created.getStatus());
        assertEquals("CUSTOMER", created.getRole());
    }

    @Test
    void createAccountRejectsDuplicateLogin() {
        when(accountRepository.findByLogin("alice")).thenReturn(Optional.of(sampleAccount()));

        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "alice", "12345", "Alice", BigDecimal.TEN, "ACTIVE", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsBlankLogin() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "   ", "12345", "User", BigDecimal.TEN, "ACTIVE", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsInvalidPin() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "user", "12", "User", BigDecimal.TEN, "ACTIVE", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsBlankHolderName() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "user", "12345", "   ", BigDecimal.TEN, "ACTIVE", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsNullBalance() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "user", "12345", "User", null, "ACTIVE", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "user", "12345", "User", BigDecimal.valueOf(-1), "ACTIVE", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "user", "12345", "User", BigDecimal.TEN, "LOCKED", "CUSTOMER"));
    }

    @Test
    void createAccountRejectsInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAccount(
                "user", "12345", "User", BigDecimal.TEN, "ACTIVE", "MANAGER"));
    }

    @Test
    void deleteAccountReturnsTrueForExistingAccount() {
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(sampleAccount()));
        when(accountRepository.deleteAccount(1)).thenReturn(true);

        assertTrue(adminService.deleteAccount(1));
    }

    @Test
    void deleteAccountRejectsZeroAccountNumber() {
        assertThrows(IllegalArgumentException.class, () -> adminService.deleteAccount(0));
    }

    @Test
    void deleteAccountRejectsNegativeAccountNumber() {
        assertThrows(IllegalArgumentException.class, () -> adminService.deleteAccount(-5));
    }

    @Test
    void deleteAccountRejectsMissingAccount() {
        when(accountRepository.findByAccountNumber(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> adminService.deleteAccount(99));
    }

    @Test
    void updateAccountUsesImmutableProfileCopy() {
        Account existing = sampleAccount();
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(existing));
        when(accountRepository.findByLogin("alice2")).thenReturn(Optional.empty());
        when(accountRepository.updateAccount(any(Account.class))).thenReturn(true);

        boolean updated = adminService.updateAccount(1, "alice2", "22222", "Alice B", "DISABLED", "CUSTOMER");

        assertTrue(updated);
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).updateAccount(captor.capture());
        assertEquals("alice2", captor.getValue().getLogin());
        assertEquals("DISABLED", captor.getValue().getStatus());
    }

    @Test
    void updateAccountRejectsZeroAccountNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> adminService.updateAccount(0, "alice", "11111", "Alice", "ACTIVE", "CUSTOMER"));
    }

    @Test
    void updateAccountRejectsMissingAccount() {
        when(accountRepository.findByAccountNumber(99)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> adminService.updateAccount(99, "alice", "11111", "Alice", "ACTIVE", "CUSTOMER"));
    }

    @Test
    void updateAccountRejectsDuplicateLoginOwnedByAnotherAccount() {
        Account existing = sampleAccount();
        Account other = new Account(2, "bob", "22222", "Bob Smith",
                BigDecimal.valueOf(900), "ACTIVE", "CUSTOMER");

        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(existing));
        when(accountRepository.findByLogin("bob")).thenReturn(Optional.of(other));

        assertThrows(IllegalArgumentException.class,
                () -> adminService.updateAccount(1, "bob", "11111", "Alice", "ACTIVE", "CUSTOMER"));
    }

    @Test
    void updateAccountRejectsInvalidPin() {
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(sampleAccount()));

        assertThrows(IllegalArgumentException.class,
                () -> adminService.updateAccount(1, "alice", "abc", "Alice", "ACTIVE", "CUSTOMER"));
    }

    @Test
    void updateAccountRejectsInvalidStatus() {
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(sampleAccount()));

        assertThrows(IllegalArgumentException.class,
                () -> adminService.updateAccount(1, "alice", "11111", "Alice", "LOCKED", "CUSTOMER"));
    }

    @Test
    void updateAccountRejectsInvalidRole() {
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(sampleAccount()));

        assertThrows(IllegalArgumentException.class,
                () -> adminService.updateAccount(1, "alice", "11111", "Alice", "ACTIVE", "MANAGER"));
    }

    @Test
    void searchAccountReturnsAccountWhenFound() {
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(sampleAccount()));

        Optional<Account> result = adminService.searchAccount(1);

        assertTrue(result.isPresent());
        assertEquals("alice", result.get().getLogin());
    }

    @Test
    void searchAccountReturnsEmptyWhenMissing() {
        when(accountRepository.findByAccountNumber(100)).thenReturn(Optional.empty());

        assertFalse(adminService.searchAccount(100).isPresent());
    }

    @Test
    void searchAccountRejectsZeroAccountNumber() {
        assertThrows(IllegalArgumentException.class, () -> adminService.searchAccount(0));
    }

    private Account sampleAccount() {
        return new Account(1, "alice", "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER");
    }
}