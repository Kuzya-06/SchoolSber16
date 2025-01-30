package ru.sber.recipecrud.repository;


import org.springframework.data.repository.CrudRepository;
import ru.sber.recipecrud.model.Recipe;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {


    List<Recipe> findByNameIgnoreCase(String name);

}
