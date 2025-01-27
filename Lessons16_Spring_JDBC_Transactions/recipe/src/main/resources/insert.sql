-- Вставка нового рецепта
INSERT INTO recipes (name) VALUES ('Штрудель с яблоками из слоеного теста классический');
-- Получение ID только что вставленного рецепта
SET @last_recipe_id = LAST_INSERT_ID();
-- Вставка ингредиентов для яблочного пирога
INSERT INTO ingredients (recipe_id, name, quantity) VALUES
                                                        (@last_recipe_id, 'яблоки', '3 шт.'),
                                                        (@last_recipe_id, 'слоёное тесто', '500г'),
                                                        (@last_recipe_id, 'мука пшеничная', '2 ст.л.'),
                                                        (@last_recipe_id, 'грецкие орехи', '0.5 ст.л.'),
                                                        (@last_recipe_id, 'яичные желтки', '1 шт.'),
                                                        (@last_recipe_id, 'вода', '1 ч.л.'),
                                                        (@last_recipe_id, 'корица', '0.5 ч.л.'),
                                                        (@last_recipe_id, 'сахар', '5 ст.л.');

-- Вставка нового рецепта
INSERT INTO recipes (name) VALUES ('Шарлотка классическая с яблоками в духовке пышная');
-- Получение ID только что вставленного рецепта
SET @last_recipe_id = LAST_INSERT_ID();
-- Вставка ингредиентов для яблочного пирога
INSERT INTO ingredients (recipe_id, name, quantity) VALUES
                                                        (@last_recipe_id, 'яблоки', '400гр'),
                                                        (@last_recipe_id, 'мука пшеничная', '170г'),
                                                        (@last_recipe_id, 'яйца', '3 шт.'),
                                                        (@last_recipe_id, 'соль', 'по вкусу'),
                                                        (@last_recipe_id, 'сахар', '150г');

-- Вставка другого нового рецепта
INSERT INTO recipes (name) VALUES ('Ромовая баба по ГОСТу');
-- Получение ID только что вставленного рецепта
SET @last_recipe_id = LAST_INSERT_ID();
-- Вставка ингредиентов для ромовой бабы
INSERT INTO ingredients (recipe_id, name, quantity) VALUES
                                                        (@last_recipe_id, 'Пшеничная мука', '250 гр'),
                                                        (@last_recipe_id, 'Яйца куриные', '2 шт.'),
                                                        (@last_recipe_id, 'Дрожи', '17 гр'),
                                                        (@last_recipe_id, 'ром', '70 мл'),
                                                        (@last_recipe_id, 'сахар', '10 гр'),
                                                        (@last_recipe_id, 'соль', '5 гр'),
                                                        (@last_recipe_id, 'изюм', '50 гр'),
                                                        (@last_recipe_id, 'маргарин', '100 гр');