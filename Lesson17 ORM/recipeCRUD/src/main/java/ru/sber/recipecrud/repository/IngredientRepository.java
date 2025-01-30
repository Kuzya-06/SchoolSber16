package ru.sber.recipecrud.repository;

import org.springframework.data.repository.CrudRepository;
import ru.sber.recipecrud.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {

}