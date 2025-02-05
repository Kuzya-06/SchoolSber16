package ru.sber.atm.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.atm.exception.AccountLockedException;
import ru.sber.atm.exception.TerminalException;
import ru.sber.atm.model.Terminal;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;
import ru.sber.atm.repository.TerminalRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//@SessionScope
@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class TerminalService {

    private final TerminalRepository terminalRepository;

    private final Map<String, Integer> attemptsMap = new HashMap<>();
    private final Map<String, LocalDateTime> lockEndTimeMap = new HashMap<>();


    public String validatePin(PinRequest request) {
        String card = request.getCard();
        String pin = request.getPin();
        log.info("Validating card: {}, pin: {}", card, pin);

        if (isLocked(card)) {
            log.error("Card {} is blocked. Try later.", card);
            throw new AccountLockedException("Заблокировано. Попробуйте позже.");
        }
        Optional<Terminal> terminal = terminalRepository.findByCard(card);
        if (terminal.isEmpty()) {
            log.error("Card {} not found.", card);
            throw new AccountLockedException("Такой карты нет. Попробуйте позже.");
        }

        if (pin.equals(terminal.get().getPin())) {
            attemptsMap.remove(card);
            log.info("Access granted for card {}", card);
            return "Доступ разрешен";
        } else {
            int attempts = attemptsMap.getOrDefault(card, 0) + 1;
            attemptsMap.put(card, attempts);

            if (attempts >= 3) {
                lockEndTimeMap.put(card, LocalDateTime.now().plusSeconds(10));
                attemptsMap.remove(card);
                log.warn("Card {} blocked for 10 seconds", card);
                throw new AccountLockedException("Карта заблокирована на 10 секунд.");
            }

            log.warn("Incorrect PIN for card {}. Attempts left: {}", card, (3 - attempts));
            throw new AccountLockedException("Неверный PIN. Осталось попыток: " + (3 - attempts));

        }
    }


    @Transactional(readOnly = true)
    public long getBalance(String card) {
        Terminal terminal = getTerminalByCard(card);
        log.info("Fetching balance for card {}: {}", card, terminal.getBalance());
        return terminal.getBalance();
    }

    @Transactional
    public String deposit(AmountRequest request) {
        Terminal terminal = getTerminalByCard(request.getCard());
        int amount = request.getAmount();

        if (amount % 100 != 0) {
            log.debug("Сумма должна быть кратна 100.");
            throw new TerminalException("Сумма должна быть кратна 100.");
        }

        long newBalance = terminal.getBalance() + amount;
        terminal.setBalance(newBalance);
        terminalRepository.save(terminal);
        log.info("Deposit: {} RUB to card {}. New balance: {}", amount, request.getCard(), newBalance);

        return "Операция выполнена!";
    }

    @Transactional
    public String withdraw(AmountRequest request) {
        Terminal terminal = getTerminalByCard(request.getCard());
        int amount = request.getAmount();

        if (amount % 100 != 0) {
            log.debug("Withdraw amount is not multiple of size 100");
            throw new TerminalException("Сумма должна быть кратна 100.");
        }

        if (amount > terminal.getBalance()) {
            log.debug("Withdraw amount is greater than balance");
            throw new TerminalException("Недостаточно средств. Баланс: " + terminal.getBalance());
        }

        long newBalance = terminal.getBalance() - amount;
        terminal.setBalance(newBalance);
        terminalRepository.save(terminal);
        log.info("Withdraw: {} RUB from card {}. New balance: {}", amount, request.getCard(), newBalance);

        return "Операция выполнена!";
    }

    private Terminal getTerminalByCard(String card) {
        log.debug("getTerminalByCard(String card) = {}", card);
        return terminalRepository.findByCard(card).orElseThrow(() -> new TerminalException("Карта не найдена."));
    }

    private boolean isLocked(String card) {
        return lockEndTimeMap.containsKey(card) && LocalDateTime.now().isBefore(lockEndTimeMap.get(card));
    }
}
