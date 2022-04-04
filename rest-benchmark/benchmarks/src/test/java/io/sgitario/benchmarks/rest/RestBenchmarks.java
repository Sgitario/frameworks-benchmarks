package io.sgitario.benchmarks.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.http.HttpResponse;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.jcloud.api.HttpService;
import io.jcloud.api.JCloud;
import io.jcloud.api.Quarkus;
import io.jcloud.api.Spring;
import io.jcloud.core.EnableBenchmark;
import io.jcloud.core.ServiceState;

@JCloud
@Fork(3)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@Threads(50)
public abstract class RestBenchmarks implements EnableBenchmark {

    @Quarkus(location = "../quarkus-resteasy-reactive")
    public static HttpService quarkusReactive = new HttpService().setAutoStart(false);

    public static class QuarkusResteasyReactiveState extends ServiceState<HttpService> {

        public QuarkusResteasyReactiveState() {
            super(quarkusReactive);
        }
    }

    @Quarkus(location = "../quarkus-resteasy-classic")
    public static HttpService quarkusClassic = new HttpService().setAutoStart(false);

    public static class QuarkusResteasyClassicState extends ServiceState<HttpService> {

        public QuarkusResteasyClassicState() {
            super(quarkusClassic);
        }
    }

    @Spring(location = "../spring-boot-web")
    public static HttpService springWeb = new HttpService().setAutoStart(false);

    public static class SpringWebState extends ServiceState<HttpService> {

        public SpringWebState() {
            super(springWeb);
        }
    }

    @Benchmark
    public String quarkusResteasyReactive(QuarkusResteasyReactiveState state) {
        return runBenchmark(state);
    }

    @Benchmark
    public String quarkusResteasyClassic(QuarkusResteasyClassicState state) {
        return runBenchmark(state);
    }

    @Benchmark
    public String springWeb(SpringWebState state) {
        return runBenchmark(state);
    }

    private String runBenchmark(ServiceState<HttpService> state) {
        HttpResponse<String> response = state.getService().getString("/ping");
        assertEquals("pong", response.body());
        return response.body();
    }
}
