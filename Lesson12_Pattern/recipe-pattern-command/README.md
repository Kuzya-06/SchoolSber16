# Домашнее задание 12
## Найти в своем коде нереализованный паттерн (код написан «в лоб», без применения паттерна), реализовать его.

## Применение паттерна Команда (Command)
Данный паттерн применен к проекту `Lessons16_Spring_JDBC_Transactions`  
Этот паттерн упрощает обработку пользовательских команд (добавление, поиск, удаление рецептов и т.д.) и сделал код 
более модульным.
* Вся логика обработки команд (добавление, поиск, удаление) находилась в одном методе run, что делало его сложным для 
поддержки и расширения. Если потребуется добавить новую команду, придется изменять метод run, что нарушит принцип 
открытости/закрытости (Open/Closed Principle).
* Убрал повторяющийся код
* Упрощает тестирование

# Шаги реализации

</b></details>
<details><summary>1. Интерфейс Command</summary>

```java
public interface Command {
    void execute();
}
```
</details>


</b></details>
<details><summary>2. Классы для каждой команды</summary>

```java
public class AddRecipeCommand implements Command {
    private final RecipeManager recipeManager;
    private final Scanner scanner;

    public AddRecipeCommand(RecipeManager recipeManager, Scanner scanner) {
        this.recipeManager = recipeManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Введите имя рецепта: ");
        String name = scanner.nextLine();
        while (name.isEmpty()) {
            System.out.print("Должно быть введено наименование рецепта \n");
            name = scanner.nextLine();
        }

        System.out.print("Введите ингредиенты (имя-количество), разделенные запятыми: \n");
        String ingredientsInput = scanner.nextLine();
        while (ingredientsInput.isEmpty()) {
            System.out.print("Должны быть введены ингредиенты \n");
            ingredientsInput = scanner.nextLine();
        }

        List<String> ingredients = validateIngredients(ingredientsInput);
        int recipeId = recipeManager.addRecipe(name, ingredients);
        System.out.println("Рецепт добавлен! ID: " + recipeId);
    }

    private List<String> validateIngredients(String ingredientsInput) {
        String[] ingredientsArray = ingredientsInput.contains(",") 
            ? ingredientsInput.split(",") 
            : new String[]{ingredientsInput};

        List<String> ingredients = new ArrayList<>();
        for (String ingredient : ingredientsArray) {
            ingredients.add(ingredient.trim());
        }
        return ingredients;
    }
}
```
</details>

</b></details>
<details><summary>3. CommandExecutor</summary>

```java
public class CommandExecutor {
    private final Map<Integer, Command> commands = new HashMap<>();
    private final Scanner scanner;

    public CommandExecutor(Scanner scanner) {
        this.scanner = scanner;
    }

    public void registerCommand(int choice, Command command) {
        commands.put(choice, command);
    }

    public void executeCommand(int choice) {
        Command command = commands.get(choice);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Неверный выбор. Попробуйте снова.");
        }
    }
}
```
</details>

### Функциональность:
* Добавление рецепта - рецепт состоит из множества ингредиентов и их количественного состава
* Поиск рецепта по имени или части имени блюда
* Удаление блюда

1. Запустить Docker

####   для Postgres
2. Запустить следующую команду
```bash
docker run --name cashDB -p 5433:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -v c:\postgresdocker:/var/lib/postgresql/data --restart=unless-stopped postgres:16
```

####   для MySQL
2. Запустить следующую команду
```bash
docker run -d --name mysqlRecipeDB -e MYSQL_ROOT_PASSWORD=123 -e MYSQL_USER=admin -e MYSQL_PASSWORD=123 -e MYSQL_DATABASE=recipeDB -v c:\mySQLDocker:/var/lib/mysql -p 3307:3306 mysql:9.0
```
С помощью `create.sql` установить и `insert.sql` заполнить БД.
3. Запустить приложение

4. Чтобы смотреть log транзакции запустите приложение с профилем debug
