package ru.sber;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static java.util.Optional.empty;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Работает до 22.02.2025. После этой даты надо оплатить на cloud.ru
 * либо запустить <a href="https://github.com/test80git/cloud-worker-app/">...</a>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssuredApplicationTests {
    private int[] array = new int[1];

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://app-worker2.containerapps.ru";
    }

    @Epic("TESTING FOR https://app-worker2.containerapps.ru/worker")
    @Feature(value = "Tests 0")
    @Severity(SeverityLevel.BLOCKER)
    @Description("При получении работников, затем статус 200 и типа контента JSON")
    @Story(value = "Test  with correct credentials")
    @Test
    @Order(0)
    public void whenGetWorkers_thenStatus200AndContentTypeJson() {
        given().when()
                .get("/worker")
                .then()
                .statusCode(200)
                .and()
                .contentType(ContentType.JSON);
    }


    @Epic("TESTING FOR https://app-worker2.containerapps.ru/worker")
    @Feature(value = "Tests 1")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Когда получайте работников, то тело не пусто")
    @Story(value = "Test  with correct credentials")
    @Test
    @Order(1)
    public void whenGetWorkers_thenBodyNotEmpty() {
        given().when()
                .get("/worker")
                .then()
                .body(not(empty()));
    }

    @Epic("TESTING FOR https://app-worker2.containerapps.ru/worker")
    @Feature(value = "Tests 2")
    @Severity(SeverityLevel.BLOCKER)
    @Description("При получении рабочих, затем исправьте структуру JSON")
    @Story(value = "Test  with correct credentials")
    @Test
    @Order(2)
    public void whenGetWorkers_thenCorrectJsonStructure() {
        given().when()
                .get("/worker")
                .then()
                .body("$", hasSize(greaterThan(0)))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].surName", notNullValue())
                .body("[0].dateOfBirth", notNullValue())
                .body("[0].address", notNullValue())
                .body("[0].create", notNullValue())
                .body("[0].update", notNullValue())
        ;
    }

    @Epic("TESTING FOR https://app-worker2.containerapps.ru/worker/new")
    @Feature(value = "Tests 3")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Создать работника, затем статус создан")
    @Story(value = "Test  with correct credentials")
    @Test
    @Order(3)
    void createWorker_thenStatusCreated() {
        String requestBody = """
                {
                    "name": "John",
                    "surName": "Doe",
                    "dateOfBirth": "1990-05-15",
                    "address": "123 Main St"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/worker/new")
                .then()
                .statusCode(201) // Ожидаем статус CREATED
                .body("name", equalTo("John"))
                .body("surName", equalTo("Doe"))
                .body("dateOfBirth", equalTo("1990-05-15"))
                .body("address", equalTo("123 Main St"))
                .extract()
                .response();

        int workerId = response.jsonPath().getInt("id");
        System.out.println("Created worker ID: " + workerId);
        given().when()
                .delete("/worker/delete/" + workerId); // удаление созданного Worker
    }


    @Epic("TESTING FOR https://app-worker2.containerapps.ru/worker/new")
    @Feature(value = "Tests 4")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Создать работника, затем сбой проверки")
    @Story(value = "Test  with no correct credentials")
    @Test
    @Order(4)
    void createWorker_thenValidationFailure() {
        String requestBody = """
                {
                    "name": "",
                    "surName": "",
                    "dateOfBirth": "2100-01-01",
                    "address": ""
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/worker/new")
                .then()
                .statusCode(401) // Здесь специально указываем статус 401, который не правильный. Должен быть 400
                .body(containsString("Name must not be blank"))
                .body(containsString("Surname must not be blank"))
                .body(containsString("Date of birth must be in the past"))
                .body(containsString("Address must not be blank"))
        ;
    }

    @Epic("TESTING FOR https://app-worker2.containerapps.ru/worker")
    @Feature(value = "Tests 5")
    @Severity(SeverityLevel.BLOCKER)
    @Description("При получении работников содержится конкретный работник")
    @Story(value = "Test  with no correct credentials")
    @Test
    @Order(5)
    public void whenGetWorkers_thenContainsSpecificWorker() {
        given().when()
                .get("/worker")
                .then()
                .body("name", hasItem("Tom"));
    }

}
