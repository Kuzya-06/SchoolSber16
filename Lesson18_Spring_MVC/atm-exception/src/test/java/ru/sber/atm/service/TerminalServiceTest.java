package ru.sber.atm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.sber.atm.exception.AccountLockedException;
import ru.sber.atm.exception.TerminalException;
import ru.sber.atm.model.Terminal;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;
import ru.sber.atm.repository.TerminalRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class TerminalServiceTest {

    @Mock
    private TerminalRepository terminalRepository;

    @InjectMocks
    private TerminalService terminalService;
    private Terminal terminal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        terminal = new Terminal(1L, "123456", "1234", 5000L, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Проверка успешной валидации PIN")
    void validatePin_CorrectPin_ReturnsAccessAllowed() {
        PinRequest pinRequest = new PinRequest("123456", "1234");

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        String result = terminalService.validatePin(pinRequest);
        assertEquals("Доступ разрешен", result);
    }

    @Test
    @DisplayName("Проверка обработки неверного PIN")
    void validatePin_IncorrectPin_ThrowsException() {
        PinRequest pinRequest = new PinRequest("123456", "0000");

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        AccountLockedException exception = assertThrows(AccountLockedException.class, () -> terminalService.validatePin(pinRequest));

        assertEquals("Неверный PIN. Осталось попыток: 2", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка блокировки карты после трех неудачных попыток ввода PIN")
    void validatePin_LockAfterThreeFailedAttempts() {
        PinRequest pinRequest1 = new PinRequest("123456", "0001");
        PinRequest pinRequest2 = new PinRequest("123456", "0002");
        PinRequest pinRequest3 = new PinRequest("123456", "0003");

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        assertThrows(AccountLockedException.class, () -> terminalService.validatePin(pinRequest1));
        assertThrows(AccountLockedException.class, () -> terminalService.validatePin(pinRequest2));
        AccountLockedException exception = assertThrows(AccountLockedException.class, () -> {
            terminalService.validatePin(pinRequest3);
        });

        assertEquals("Карта заблокирована на 10 секунд.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка успешного получения баланса")
    void getBalance_ReturnsInitialBalance() {
        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        long balance = terminalService.getBalance("123456");

        assertEquals(5000L, balance);
    }

    @Test
    @DisplayName("Проверка успешного депозита на счет")
    void deposit_ValidAmount_IncreasesBalance() {
        AmountRequest depositRequest = new AmountRequest("123456", 500);

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        String result = terminalService.deposit(depositRequest);

        assertEquals("Операция выполнена!", result);
        assertEquals(5500L, terminal.getBalance()); // После депозита баланс должен увеличиться на 500

    }

    @Test
    @DisplayName("Проверка обработки депозита, если сумма не кратна 100")
    void deposit_AmountNotMultipleOf100() {
        AmountRequest depositRequest = new AmountRequest("123456", 150);

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        TerminalException exception = assertThrows(TerminalException.class, () -> terminalService.deposit(depositRequest));

        assertEquals("Сумма должна быть кратна 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка успешного снятия денег со счета")
    void withdraw_ValidAmount_DecreasesBalance() {
        AmountRequest withdrawRequest = new AmountRequest("123456", 1000);

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        String result = terminalService.withdraw(withdrawRequest);

        assertEquals("Операция выполнена!", result);
        assertEquals(4000L, terminal.getBalance()); // После снятия баланс должен уменьшиться на 1000

    }

    @Test
    @DisplayName("Проверка обработки снятия, если сумма не кратна 100")
    void withdraw_AmountNotMultipleOf100() {
        AmountRequest withdrawRequest = new AmountRequest("123456", 150);

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        TerminalException exception = assertThrows(TerminalException.class, () -> terminalService.withdraw(withdrawRequest));

        assertEquals("Сумма должна быть кратна 100.", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка обработки ошибки, если на счете недостаточно средств")
    void withdraw_InsufficientFunds() {
        AmountRequest withdrawRequest = new AmountRequest("123456", 6000);

        when(terminalRepository.findByCard("123456")).thenReturn(Optional.of(terminal));

        TerminalException exception = assertThrows(TerminalException.class, () -> terminalService.withdraw(withdrawRequest));

        assertEquals("Недостаточно средств. Баланс: 5000", exception.getMessage());
    }
}
