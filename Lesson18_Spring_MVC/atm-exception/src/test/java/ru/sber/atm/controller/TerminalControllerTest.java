package ru.sber.atm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
        PinRequest request = new PinRequest();
        request.setPin("1234");

        when(terminalService.validatePin(any())).thenReturn("Доступ разрешен");

        mockMvc.perform(post("/api/validate-pin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Доступ разрешен"));

        verify(terminalService, times(1)).validatePin(any());
    }

    @Test
    void getBalance_ReturnsBalance() throws Exception {
        when(terminalService.getBalance()).thenReturn(5000);

        mockMvc.perform(get("/api/balance"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000"));

        verify(terminalService, times(1)).getBalance();
    }

    @Test
    void deposit_ValidAmount_ReturnsSuccessMessage() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(1000);

        when(terminalService.deposit(any())).thenReturn("Пополнено успешно. Новый баланс: 6000");

        mockMvc.perform(post("/api/deposit")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пополнено успешно. Новый баланс: 6000"));

        verify(terminalService, times(1)).deposit(any());
    }

    @Test
    void withdraw_ValidAmount_ReturnsSuccessMessage() throws Exception {
        AmountRequest request = new AmountRequest();
        request.setAmount(1000);

        when(terminalService.withdraw(any())).thenReturn("Снятие успешно. Новый баланс: 4000");

        mockMvc.perform(post("/api/withdraw")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Снятие успешно. Новый баланс: 4000"));

        verify(terminalService, times(1)).withdraw(any());
    }
}
