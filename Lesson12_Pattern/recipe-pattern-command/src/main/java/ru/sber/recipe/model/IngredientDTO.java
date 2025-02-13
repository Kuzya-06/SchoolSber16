package ru.sber.recipe.model;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class IngredientDTO {

    private String name;
    private String quantity;

    @Override
    public String toString() {
        return
                name + "-" + quantity;
    }
}
