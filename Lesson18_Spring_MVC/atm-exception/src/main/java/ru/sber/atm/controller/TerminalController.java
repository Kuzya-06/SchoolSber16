package ru.sber.atm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;
import ru.sber.atm.service.TerminalService;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api")
@Tag(name = "ATM. Основные команды для работы с ATM")
public class TerminalController {

    private static final Logger log = LoggerFactory.getLogger(TerminalController.class);
    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Operation(summary = "Валидация PIN", description = "Проверка правильного PIN")
    @PostMapping("/validate-pin")
    public String validatePin(@RequestBody PinRequest request) {
        log.debug("Validate pin request: {}", request);
        return terminalService.validatePin(request);
    }

    @Operation(summary = "Проверка баланса", description = "Проверка баланса")
    @GetMapping("/balance/{card}")
    public long getBalance(@PathVariable String card) {
        log.debug("Begin method Controller.getBalance() card = {}", card);
        long balance = terminalService.getBalance(card);
        log.debug("Get for card = {} balance = {}", card, balance);
        return balance;
    }

    @Operation(summary = "Депозит", description = "Положить определённую сумму на счёт")
    @PostMapping("/deposit")
    public String deposit(@RequestBody AmountRequest request) {
        log.debug("Deposit request: {}", request);
        return terminalService.deposit(request);
    }

    @Operation(summary = "Снятие", description = "Снять определённую сумму со счёта")
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody AmountRequest request) {
       log.debug("Withdraw request: {}", request);
        return terminalService.withdraw(request);
    }

}


