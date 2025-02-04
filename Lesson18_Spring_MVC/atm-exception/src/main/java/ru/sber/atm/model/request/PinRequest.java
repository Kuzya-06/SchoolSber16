package ru.sber.atm.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PinRequest {
    @Schema(example = "1234", description = "PIN code")
    private String pin;
}
