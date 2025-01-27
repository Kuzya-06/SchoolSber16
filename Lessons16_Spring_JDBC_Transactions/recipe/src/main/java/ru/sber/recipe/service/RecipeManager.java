package ru.sber.recipe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.recipe.exceptions.InvalidInputException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RecipeManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RecipeManager(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    public int addRecipe(String name, List<String> ingredients) {
        log.debug("List<String> ingredients = {}", ingredients);
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("Имя рецепта не может быть пустым или null");
        }
        if (ingredients == null || ingredients.isEmpty()) {
            throw new InvalidInputException("Ингредиенты не могут быть пустыми или null");
        }
        for (String ingredient : ingredients) {
            String[] parts = ingredient.split("-");
            log.debug("parts = {}", Arrays.asList(parts));
            if (parts.length < 2) {
                throw new InvalidInputException("Ингредиент должен быть в формате 'Имя-Количество'. Некорректный: " + ingredient);
            }
        }
        // Добавление рецепта
        String insertRecipeSQL = "INSERT INTO recipes (name) VALUES (?)";
        jdbcTemplate.update(insertRecipeSQL, name);
        int recipeId = getId();
        log.debug("recipeId = {}; recipeName = {}", recipeId, name);

        // Добавление ингредиентов
        String insertIngredientSQL = "INSERT INTO ingredients (recipe_id, name, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(insertIngredientSQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ingredientPs, int i) throws SQLException {
                String ingredient = ingredients.get(i);
                String[] parts = ingredient.split("-");
                ingredientPs.setInt(1, recipeId);
                ingredientPs.setString(2, parts[0].trim());
                ingredientPs.setString(3, parts.length == 2 ? parts[1].trim() : "0");
            }

            @Override
            public int getBatchSize() {
                return ingredients.size();
            }
        });


        return recipeId;
    }

    // ----------Проблема 1+N-------------

    //    @Transactional(readOnly = true)
//    public List<String> searchRecipeByName(String name) throws SQLException {
//        List<String> recipes = new ArrayList<>();
//        String newName = "%" + name + "%";
//        String sql = "SELECT id, name FROM recipes WHERE name LIKE ?";
//        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, newName);
//
//            for (Map<String, Object> map : list) {
//                int idRecipe = (int) map.get("id");
//                String nameRecipe = (String) map.get("name");
//                log.debug("idRecipe = {}", idRecipe);
//                log.debug("nameRecipe = {}", nameRecipe);
//                List<IngredientDTO> searchIngredientDTOS = searchIngredients(idRecipe);
//                recipes.add(nameRecipe + " (ID = " + idRecipe + "): " + searchIngredientDTOS);
//            }
//        return recipes;
//    }

    @Transactional(readOnly = true)
    public List<String> searchRecipeByName(String name) {
        List<String> recipes = new ArrayList<>();
        String newName = "%" + name + "%";
        String sql = "SELECT r.id AS recipe_id, r.name AS recipe_name, i.name AS ingredient_name, i.quantity " +
                     "FROM recipes r " +
                     "LEFT JOIN ingredients i ON r.id = i.recipe_id " +
                     "WHERE r.name LIKE ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, newName);
        log.debug("list= {}", list);
        Map<Integer, List<Map<String, Object>>> groupedByRecipeId = list.stream()
                .collect(Collectors.groupingBy(map -> (Integer) map.get("recipe_id")));
// Вывод результата
        groupedByRecipeId.forEach((key, value) -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(value.get(0).get("recipe_name")).append(" (").append(key).append("): ");
            value.forEach(ingredient -> stringBuilder.append(ingredient.get("ingredient_name")).append("-")
                    .append(ingredient.get("quantity")).append(", "));
            recipes.add(stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString());
        });

        return recipes;
    }

    @Transactional
    public int deleteByIdRecipe(int idToDelete) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        int del = jdbcTemplate.update(sql, idToDelete);
        log.debug("deleteByIdRecipe {}", del);
        return del;
    }

    @Transactional
    public int deleteByNameRecipe(String nameToDelete) {
        String sql = "DELETE FROM recipes WHERE name = ?";
        int del = jdbcTemplate.update(sql, nameToDelete);
        log.debug("deleteByNameRecipe {}", del);
        return del;
    }

    public List<String> searchByIngredientName(String searchName) {
        List<Integer> recipes = new ArrayList<>();
        String newName = "%" + searchName + "%";
        String sql = "SELECT recipe_id FROM ingredients WHERE name LIKE ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, newName);
        for (Map<String, Object> map : list) {
            int idRecipe = (int) map.get("recipe_id");
            log.debug("idRecipe = {}", idRecipe);
            recipes.add(idRecipe);
        }
        return searchRecipeById(recipes);
    }

    // ----------Проблема 1+N-------------
//    private List<IngredientDTO> searchIngredients(int id) {
//
//        // Поиск ингредиентов
//        String selectIngredientSQL = "SELECT name, quantity FROM ingredients  WHERE recipe_id IN (:id)";
//        List<IngredientDTO> list = namedParameterJdbcTemplate.query(selectIngredientSQL,
//                new MapSqlParameterSource("id", id),
//                (rs, rowNum) -> new IngredientDTO(rs.getString("name"), rs.getString("quantity")));
//        return list;
//    }

    private int getId() {
        String sql = "SELECT id FROM recipes ORDER BY id DESC LIMIT 1";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return (int) list.get(0).get("id");
    }


    private List<String> searchRecipeById(List<Integer> recipesId) {
        if (recipesId == null || recipesId.isEmpty()) {
            return Collections.emptyList(); // Возвращаем пустой список, если входной список пуст
        }

        // Создаем параметры для запроса
        Map<String, Object> params = new HashMap<>();
        String sql = "SELECT id, name FROM recipes WHERE id IN (:ids)";

        // Добавляем параметры
        params.put("ids", recipesId);

        // Выполняем запрос
        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, params);

        // Извлекаем результаты
        List<String> recipeNames = new ArrayList<>();
        for (Map<String, Object> row : results) {
            recipeNames.add( row.get("name") + " (" + row.get("id") + ")");
        }

        return recipeNames;
    }

}
