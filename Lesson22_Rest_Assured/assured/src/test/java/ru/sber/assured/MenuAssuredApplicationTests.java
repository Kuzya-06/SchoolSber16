package ru.sber.assured;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.util.Optional.empty;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;


class MenuAssuredApplicationTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";

    }

    @Test
    public void whenGetMenu_thenStatus200AndContentTypeHTML() {
        given().when()
                .get("/menu")
                .then()
                .statusCode(200)
                .contentType(ContentType.HTML)
//              .contentType(ContentType.JSON) // Expected content-type "JSON" doesn't match actual content-type
        // "text/html;charset=UTF-8".
        ;
    }

    @Test
    public void whenGetMenu_thenBodyNotEmpty() {
        given().when()
                .get("/menu")
                .then()
                .body(not(empty()));
    }


    @Test
    void testCreateOrUpdateMenuItem() {

       given()
                .contentType(ContentType.JSON)
                .formParam("name", "Pizza Margherita")
                .formParam("description", "Classic Italian pizza")
                .formParam("price", 12.99)
                .formParam("page", 0)
            .formParam("sortBy", "name")
                .formParam("direction", "asc")
                .when()
                .post("/menu")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

}
