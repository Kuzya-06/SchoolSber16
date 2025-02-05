package ru.sber.atm.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AmountRequest {
    private String card;
    private int amount;
}