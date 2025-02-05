package ru.sber.atm.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PinRequest {
    @Schema(example = "123456", description = "№ карты")
    private String card;

    @Schema(example = "1234", description = "PIN code")
    private String pin;


}
