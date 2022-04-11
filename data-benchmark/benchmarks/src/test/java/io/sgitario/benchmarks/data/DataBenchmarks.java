package io.sgitario.benchmarks.data;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import java.util.Collections;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.jcloud.api.DatabaseService;
import io.jcloud.api.JCloud;
import io.jcloud.api.PostgresqlContainer;
import io.jcloud.api.Quarkus;
import io.jcloud.api.RestService;
import io.jcloud.api.Spring;
import io.jcloud.core.EnableBenchmark;
import io.jcloud.core.ServiceState;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

@JCloud
@Fork(3)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(50)
public abstract class DataBenchmarks implements EnableBenchmark {

    private static final String FRUITS_PATH = "/api/fruits";

    @PostgresqlContainer
    public static DatabaseService database = new DatabaseService();

    @Quarkus(location = "../quarkus")
    public static RestService quarkus = new RestService().setAutoStart(false)
            .withProperty("quarkus.datasource.username", database.getUser())
            .withProperty("quarkus.datasource.password", database.getPassword())
            .withProperty("quarkus.datasource.reactive.url", database::getReactiveUrl);

    public static class QuarkusState extends ServiceState<RestService> {

        public QuarkusState() {
            super(quarkus);
        }
    }

    @Spring(location = "../spring-boot")
    public static RestService spring = new RestService().setAutoStart(false)
            .withProperty("spring.datasource.url", database::getJdbcUrl)
            .withProperty("spring.datasource.username", database::getUser)
            .withProperty("spring.datasource.password", database::getPassword);

    public static class SpringState extends ServiceState<RestService> {

        public SpringState() {
            super(spring);
        }
    }

    @Benchmark
    public ValidatableResponse quarkus(QuarkusState state) {
        return runBenchmark(state);
    }

    @Benchmark
    public ValidatableResponse spring(SpringState state) {
        return runBenchmark(state);
    }

    private ValidatableResponse runBenchmark(ServiceState<RestService> state) {
        Integer id =
                state.getService().given()
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

        state.getService().given()
                .get(String.format("%s/%d",FRUITS_PATH, id))
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Lemon"));

        return state.getService().given()
                .delete(String.format("%s/%d",FRUITS_PATH, id))
                .then()
                .statusCode(204);
    }
}
