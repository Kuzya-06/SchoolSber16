package ru.sber.atm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sber.atm.exception.AccountLockedException;
import ru.sber.atm.exception.TerminalException;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;

import static org.junit.jupiter.api.Assertions.*;

class TerminalServiceTest {

    private TerminalService terminalService;

    @BeforeEach
    void setUp() {
        terminalService = new TerminalService();
    }

    @Test
    void validatePin_CorrectPin_ReturnsAccessAllowed() {
        PinRequest request = new PinRequest();
        request.setPin("1234");

        String result = terminalService.validatePin(request);
        assertEquals("Доступ разрешен", result);
    }

    @Test
    void validatePin_IncorrectPin_ThrowsException() {
        PinRequest request = new PinRequest();
        request.setPin("0000");

        assertThrows(AccountLockedException.class, () -> terminalService.validatePin(request));
        assertThrows(AccountLockedException.class, () -> terminalService.validatePin(request));
        AccountLockedException exception = assertThrows(AccountLockedException.class, () ->
                terminalService.validatePin(request));

        assertEquals("Заблокировано на 10 секунд", exception.getMessage());
    }

    @Test
    void getBalance_ReturnsInitialBalance() {
        assertEquals(5000, terminalService.getBalance());
    }

    @Test
    void deposit_ValidAmount_IncreasesBalance() {
        AmountRequest request = new AmountRequest();
        request.setAmount(1000);

        String result = terminalService.deposit(request);

        assertEquals("Пополнено успешно. Новый баланс: 6000", result);
        assertEquals(6000, terminalService.getBalance());
    }

    @Test
    void deposit_InvalidAmount_ThrowsException() {
        AmountRequest request = new AmountRequest();
        request.setAmount(150);

        Exception exception = assertThrows(TerminalException.class, () -> terminalService.deposit(request));

        assertEquals("Сумма должна быть кратна 100.", exception.getMessage());
    }

    @Test
    void withdraw_ValidAmount_DecreasesBalance() {
        AmountRequest request = new AmountRequest();
        request.setAmount(1000);

        String result = terminalService.withdraw(request);

        assertEquals("Снятие успешно. Новый баланс: 4000", result);
        assertEquals(4000, terminalService.getBalance());
    }

    @Test
    void withdraw_InvalidAmount_ThrowsException() {
        AmountRequest request = new AmountRequest();
        request.setAmount(150);

        Exception exception = assertThrows(TerminalException.class, () -> terminalService.withdraw(request));

        assertEquals("Сумма должна быть кратна 100.", exception.getMessage());
    }

    @Test
    void withdraw_InsufficientBalance_ThrowsException() {
        AmountRequest request = new AmountRequest();
        request.setAmount(6000);

        Exception exception = assertThrows(TerminalException.class, () -> terminalService.withdraw(request));

        assertEquals("Недостаточно средств. Баланс: 5000", exception.getMessage());
    }
}
