package atm.service;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import atm.model.Account;
import atm.repository.IAccountRepository;
import atm.repository.ITransactionRepository;

class CustomerServiceTest {
    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final ITransactionRepository transactionRepository = Mockito.mock(ITransactionRepository.class);
    private final CustomerService customerService = new CustomerService(accountRepository, transactionRepository);

    @Test
    void refreshAccountReturnsLatestAccountWhenFound() {
        Account latest = sampleAccount().withBalance(BigDecimal.valueOf(999));
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(latest));

        Account result = customerService.refreshAccount(sampleAccount());

        assertEquals(BigDecimal.valueOf(999), result.getBalance());
    }

    @Test
    void refreshAccountRejectsNullAccount() {
        assertThrows(IllegalArgumentException.class, () -> customerService.refreshAccount(null));
    }

    @Test
    void depositReturnsUpdatedAccount() {
        Account account = sampleAccount();
        when(accountRepository.updateBalance(1, BigDecimal.valueOf(1300))).thenReturn(true);
        when(transactionRepository.addTransaction(1, "DEPOSIT", BigDecimal.valueOf(100),
                BigDecimal.valueOf(1300), "Cash deposit")).thenReturn(true);

        Account result = customerService.deposit(account, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(1300), result.getBalance());
        assertEquals(BigDecimal.valueOf(1200), account.getBalance());
    }

    @Test
    void depositRejectsNullAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.deposit(null, BigDecimal.valueOf(100)));
    }

    @Test
    void depositRejectsNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.deposit(sampleAccount(), null));
    }

    @Test
    void depositRejectsZeroAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.deposit(sampleAccount(), BigDecimal.ZERO));
    }

    @Test
    void depositRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.deposit(sampleAccount(), BigDecimal.valueOf(-1)));
    }

    @Test
    void depositThrowsWhenBalanceUpdateFails() {
        when(accountRepository.updateBalance(1, BigDecimal.valueOf(1300))).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> customerService.deposit(sampleAccount(), BigDecimal.valueOf(100)));
    }

    @Test
    void withdrawReturnsUpdatedAccount() {
        Account account = sampleAccount();
        when(accountRepository.updateBalance(1, BigDecimal.valueOf(1000))).thenReturn(true);
        when(transactionRepository.addTransaction(1, "WITHDRAW", BigDecimal.valueOf(200),
                BigDecimal.valueOf(1000), "Cash withdrawal")).thenReturn(true);

        Account result = customerService.withdraw(account, BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(1000), result.getBalance());
    }

    @Test
    void withdrawRejectsNullAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.withdraw(null, BigDecimal.valueOf(100)));
    }

    @Test
    void withdrawRejectsNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.withdraw(sampleAccount(), null));
    }

    @Test
    void withdrawRejectsZeroAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.withdraw(sampleAccount(), BigDecimal.ZERO));
    }

    @Test
    void withdrawRejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.withdraw(sampleAccount(), BigDecimal.valueOf(-1)));
    }

    @Test
    void withdrawRejectsInsufficientBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.withdraw(sampleAccount(), BigDecimal.valueOf(5000)));
    }

    @Test
    void withdrawThrowsWhenBalanceUpdateFails() {
        when(accountRepository.updateBalance(1, BigDecimal.valueOf(1000))).thenReturn(false);

        assertThrows(IllegalStateException.class,
                () -> customerService.withdraw(sampleAccount(), BigDecimal.valueOf(200)));
    }

    @Test
    void displayBalanceReturnsLatestAccount() {
        Account latest = sampleAccount().withBalance(BigDecimal.valueOf(1500));
        when(accountRepository.findByAccountNumber(1)).thenReturn(Optional.of(latest));

        Account result = customerService.displayBalance(sampleAccount());

        assertEquals(BigDecimal.valueOf(1500), result.getBalance());
    }

    private Account sampleAccount() {
        return new Account(1, "alice", "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER");
    }
}