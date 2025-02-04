package ru.sber.atm.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.sber.atm.exception.AccountLockedException;
import ru.sber.atm.exception.TerminalException;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;

import java.time.LocalDateTime;

@Slf4j
@Service
@Data
public class TerminalService {

    private static final String PIN_CODE = "1234";
    private int balance = 5000;
    private int attempts = 0;
    private LocalDateTime lockEndTime = null;


    public String validatePin(PinRequest request) {
        String pin = request.getPin();
        log.info("Validating pin: {}", pin);
        if (isLocked()) {
            log.error("Blocked. Try it later.");
            throw new AccountLockedException("Заблокировано. Попробуйте позже.");
        }

        if (PIN_CODE.equals(pin)) {
            attempts = 0;
            log.info("Access is allowed");
            return "Доступ разрешен";
        } else {
            attempts++;
            if (attempts >= 3) {
                lockEndTime = LocalDateTime.now().plusSeconds(10);
                attempts = 0;
                log.debug("Blocked for 10 seconds");
                throw new AccountLockedException("Заблокировано на 10 секунд");
            }
            log.debug("Inappropriate PIN. There are attempts left: {}",(3 - attempts));
            throw new AccountLockedException("Неверный PIN. Осталось попыток: " + (3 - attempts));
        }
    }


    public int getBalance() {
        return balance;
    }


    public String deposit(AmountRequest request) {
        int amount = request.getAmount();
        log.debug("Deposit request: {}", amount);
        if (amount % 100 != 0) {
            log.debug("Deposit amount is not multiple of size 100");
            throw new TerminalException("Сумма должна быть кратна 100.");
        }
        balance += amount;
        log.info("Balance: {}", balance);
        return "Пополнено успешно. Новый баланс: " + balance;
    }


    public String withdraw(AmountRequest request) {
        int amount = request.getAmount();
        log.debug("Withdraw request: {}", amount);
        if (amount % 100 != 0) {
            log.debug("Withdraw amount is not multiple of size 100");
            throw new TerminalException("Сумма должна быть кратна 100.");
        }
        if (amount > balance) {
            log.debug("Withdraw amount is greater than balance");
            throw new TerminalException("Недостаточно средств. Баланс: " + balance);
        }
        balance -= amount;
        log.info("Balance: {}", balance);
        return "Снятие успешно. Новый баланс: " + balance;
    }

    private boolean isLocked() {
        return lockEndTime != null && LocalDateTime.now().isBefore(lockEndTime);
    }

}
