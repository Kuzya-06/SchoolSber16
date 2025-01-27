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