package ru.sber.recipecrud.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.recipecrud.model.Ingredient;
import ru.sber.recipecrud.model.Recipe;
import ru.sber.recipecrud.repository.IngredientRepository;
import ru.sber.recipecrud.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecipeService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //    @PersistenceContext
    private final EntityManager entityManager;

    private final RecipeRepository recipeRepository;


    public RecipeService(RecipeRepository recipeRepository,  EntityManager entityManager) {
        this.recipeRepository = recipeRepository;

        this.entityManager = entityManager;
    }

    @Transactional
    public Recipe addRecipe(String name, List<String> ingredientsInput) {
        Recipe recipe = new Recipe();
        recipe.setName(name);

        List<Ingredient> ingredients = ingredientsInput.stream().map(input -> {
            String[] parts = input.split("-");
            Ingredient ingredient = new Ingredient();
            ingredient.setName(parts[0].trim());
            ingredient.setQuantity(parts.length > 1 ? parts[1].trim() : "0");
            ingredient.setRecipe(recipe);
            return ingredient;
        }).collect(Collectors.toList());

        recipe.setIngredients(ingredients);
        entityManager.persist(recipe);
        return recipe;
    }

    @Transactional
    public boolean deleteRecipeById(int id) {
        Recipe recipe = entityManager.find(Recipe.class, id);
        log.debug("Найден Recipe {}", recipe);
        if (recipe != null) {
            entityManager.remove(recipe);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteRecipeByName(String name) {
        Optional<Recipe> recipeOpt = recipeRepository.findByNameIgnoreCase(name).stream().findFirst();
        recipeOpt.ifPresent(entityManager::remove);
        return recipeOpt.isPresent();
    }

    @Transactional(readOnly = true)
    public List<Recipe> searchRecipeByName(String name) {

        TypedQuery<Recipe> query = entityManager.createQuery(
                "SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :namePart, '%'))",
                Recipe.class
        );
        query.setParameter("namePart", name);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Recipe> searchByIngredientName(String searchName) {
        log.debug("Begin searchByIngredientName c searchName " + searchName);
        String sql = "SELECT DISTINCT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i.name) LIKE LOWER(:name)";
        List<Recipe> recipes = entityManager.createQuery(
                        sql, Recipe.class)
                .setParameter("name", "%" + searchName + "%")
                .getResultList();
        log.debug("End searchByIngredientName c recipes => {}", recipes);
        return recipes;
    }

}
