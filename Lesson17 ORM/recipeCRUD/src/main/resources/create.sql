
CREATE DATABASE recipeDB;

USE recipeDB;

CREATE TABLE IF NOT EXISTS recipes (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS ingredients (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             recipe_id INT,
                             name VARCHAR(255) NOT NULL,
                             quantity VARCHAR(50),
                             FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);



-- Удаление данных из таблицы
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE recipes;
TRUNCATE TABLE ingredients;
SET FOREIGN_KEY_CHECKS = 1;