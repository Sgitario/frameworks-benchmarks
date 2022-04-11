package io.sgitario.benchmarks.data;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.http.HttpResponse;
import java.util.Collections;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.jcloud.api.DatabaseService;
import io.jcloud.api.HttpService;
import io.jcloud.api.JCloud;
import io.jcloud.api.PostgresqlContainer;
import io.jcloud.api.Quarkus;
import io.jcloud.api.RestService;
import io.jcloud.api.Spring;
import io.jcloud.core.EnableBenchmark;
import io.jcloud.core.ServiceState;
import io.restassured.http.ContentType;

@Disabled
@JCloud
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
