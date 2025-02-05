package ru.sber.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.sber.atm.exception.AccountLockedException;
import ru.sber.atm.exception.TerminalException;
import ru.sber.atm.model.request.AmountRequest;
import ru.sber.atm.model.request.PinRequest;
import ru.sber.atm.service.TerminalService;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TerminalController.class)
class TerminalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerminalService terminalService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validatePin_CorrectPin_ReturnsAccessAllowed() throws Exception {
        PinRequest pinRequest = new PinRequest("123456", "1234");

        when(terminalService.validatePin(any())).thenReturn("Доступ разрешен");

        mockMvc.perform(post("/api/validate-pin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Доступ разрешен"));

        verify(terminalService, times(1)).validatePin(any());
    }

    @Test
    void validatePin_IncorrectPin() throws Exception {
        PinRequest pinRequest = new PinRequest("123456", "0000");

        when(terminalService.validatePin(pinRequest))
                .thenThrow(new AccountLockedException("Неверный PIN. Осталось попыток: 2"));

        mockMvc.perform(post("/api/validate-pin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pinRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Неверный PIN. Осталось попыток: 2"));
    }

    @Test
    void getBalance_ReturnsBalance() throws Exception {
        when(terminalService.getBalance(anyString())).thenReturn(5000L);

        mockMvc.perform(get("/api/balance/123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));

        verify(terminalService, times(1)).getBalance(anyString());
    }

    @Test
    void deposit_ValidAmount_ReturnsSuccessMessage() throws Exception {
        AmountRequest request = new AmountRequest("123456", 1000);

        when(terminalService.deposit(any())).thenReturn("Операция выполнена");

        mockMvc.perform(post("/api/deposit")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Операция выполнена"));

        verify(terminalService, times(1)).deposit(any());
    }

    @Test
    void deposit_NotMultipleOf100() throws Exception {
        AmountRequest depositRequest = new AmountRequest("123456", 150);

        when(terminalService.deposit(depositRequest))
                .thenThrow(new TerminalException("Сумма должна быть кратна 100."));

        mockMvc.perform(post("/api/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Сумма должна быть кратна 100."));
    }

    @Test
    void withdraw_ValidAmount_ReturnsSuccessMessage() throws Exception {
        AmountRequest request = new AmountRequest("123456", 1000);

        when(terminalService.withdraw(any())).thenReturn("Операция выполнена");

        mockMvc.perform(post("/api/withdraw")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Операция выполнена"));

        verify(terminalService, times(1)).withdraw(any());
    }

    @Test
    void withdraw_NotMultipleOf100() throws Exception {
        AmountRequest withdrawRequest = new AmountRequest("123456", 350);

        when(terminalService.withdraw(withdrawRequest))
                .thenThrow(new TerminalException("Сумма должна быть кратна 100."));

        mockMvc.perform(post("/api/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Сумма должна быть кратна 100."));
    }

    @Test
    void withdraw_InsufficientFunds() throws Exception {
        AmountRequest withdrawRequest = new AmountRequest("123456", 10000);

        when(terminalService.withdraw(withdrawRequest))
                .thenThrow(new AccountLockedException("Недостаточно средств. Баланс: 5000"));

        mockMvc.perform(post("/api/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Недостаточно средств. Баланс: 5000"));
    }

}
