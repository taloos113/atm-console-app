package atm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import atm.model.Account;
import atm.repository.IAccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AuthServiceTest {
    private final IAccountRepository accountRepository = Mockito.mock(IAccountRepository.class);
    private final AuthService authService = new AuthService(accountRepository);

    @Test
    void loginReturnsAccountForValidCredentials() {
        Account account = sampleAccount();
        when(accountRepository.findByLoginAndPin("alice", "11111")).thenReturn(Optional.of(account));

        Account result = authService.login("alice", "11111");

        assertEquals(account, result);
    }

    @Test
    void loginReturnsNullForInvalidPinFormat() {
        assertNull(authService.login("alice", "123"));
    }

    @Test
    void loginReturnsNullForBlankLogin() {
        assertNull(authService.login(" ", "11111"));
    }

    private Account sampleAccount() {
        return new Account(1, "alice", "11111", "Alice Johnson",
                BigDecimal.valueOf(1200), "ACTIVE", "CUSTOMER");
    }
}
