package io.sgitario.benchmarks.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.github.snowdrop.jester.api.BenchmarkConfiguration;
import io.github.snowdrop.jester.api.DatabaseService;
import io.github.snowdrop.jester.api.HttpService;
import io.github.snowdrop.jester.api.Jester;
import io.github.snowdrop.jester.api.PostgresqlContainer;
import io.github.snowdrop.jester.api.Quarkus;
import io.github.snowdrop.jester.api.Spring;
import io.github.snowdrop.jester.core.EnableBenchmark;
import io.github.snowdrop.jester.core.ServiceState;

@Jester
@Fork(3)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(50)
@BenchmarkConfiguration(outputLocation = "../results")
public abstract class DataBenchmarks implements EnableBenchmark {

    private static final String FRUITS_PATH = "/api/fruits";

    @PostgresqlContainer
    public static DatabaseService database = new DatabaseService();

    @Quarkus(location = "../quarkus")
    public static HttpService quarkus = new HttpService().setAutoStart(false)
            .withProperty("quarkus.datasource.username", database.getUser())
            .withProperty("quarkus.datasource.password", database.getPassword())
            .withProperty("quarkus.datasource.jdbc.url", database::getJdbcUrl);;

    public static class QuarkusState extends ServiceState<HttpService> {

        public QuarkusState() {
            super(quarkus);
        }
    }

    @Spring(location = "../spring-boot")
    public static HttpService spring = new HttpService().setAutoStart(false)
            .withProperty("spring.datasource.url", database::getJdbcUrl)
            .withProperty("spring.datasource.username", database::getUser)
            .withProperty("spring.datasource.password", database::getPassword);

    public static class SpringState extends ServiceState<HttpService> {

        public SpringState() {
            super(spring);
        }
    }

    @Benchmark
    public HttpResponse<InputStream> quarkus(QuarkusState state) {
        return runBenchmark(state);
    }

    @Benchmark
    public HttpResponse<InputStream> spring(SpringState state) {
        return runBenchmark(state);
    }

    private HttpResponse<InputStream> runBenchmark(ServiceState<HttpService> state) {
        Integer id =
                (Integer) state.getService().postAsJson(Collections.singletonMap("name", "Lemon"), FRUITS_PATH)
                        .body().get().get("id");

        Map fruit = state.getService().getAsJson(String.format("%s/%d",FRUITS_PATH, id))
                .body().get();

        assertEquals(id, fruit.get("id"));
        assertEquals("Lemon", fruit.get("name"));

        return state.getService().delete(String.format("%s/%d",FRUITS_PATH, id));
    }
}
