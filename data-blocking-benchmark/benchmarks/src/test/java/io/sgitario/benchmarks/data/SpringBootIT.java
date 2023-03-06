package io.sgitario.benchmarks.data;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import java.util.Collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.snowdrop.jester.api.DatabaseService;
import io.github.snowdrop.jester.api.Jester;
import io.github.snowdrop.jester.api.PostgresqlContainer;
import io.github.snowdrop.jester.api.RestService;
import io.github.snowdrop.jester.api.Spring;
import io.restassured.http.ContentType;

@Disabled
@Jester
public class SpringBootIT {

    private static final String FRUITS_PATH = "/api/fruits";

    @PostgresqlContainer
    public static DatabaseService database = new DatabaseService();

    @Spring(location = "../spring-boot")
    public static RestService spring = new RestService()
            .withProperty("spring.datasource.url", database::getJdbcUrl)
            .withProperty("spring.datasource.username", database::getUser)
            .withProperty("spring.datasource.password", database::getPassword);

    @Test
    public void testPostGetAndDelete() {
        Integer id =
                given()
                        .contentType(ContentType.JSON)
                        .body(Collections.singletonMap("name", "Lemon"))
                        .post(FRUITS_PATH)
                        .then()
                        .statusCode(201)
                        .body("id", is(not(emptyString())))
                        .body("name", is("Lemon"))
                        .extract()
                        .response()
                        .path("id");

        given()
                .get(String.format("%s/%d",FRUITS_PATH, id))
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Lemon"));

        given()
                .delete(String.format("%s/%d",FRUITS_PATH, id))
                .then()
                .statusCode(204);
    }
}
