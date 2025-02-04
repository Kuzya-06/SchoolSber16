package ru.sber.mvc.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MenuItemDto {

    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 50, message = "Название не более 50 символов")
    private String name;

    @DecimalMin(value = "1", message = "Цена должна быть больше 0", inclusive = true)
    private double price;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

}
