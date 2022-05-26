package io.sgitario.benchmarks.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.http.HttpResponse;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import io.jester.api.HttpService;
import io.jester.api.Jester;
import io.jester.api.Quarkus;
import io.jester.api.Spring;
import io.jester.core.EnableBenchmark;
import io.jester.core.ServiceState;

@Jester
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

    @Spring(location = "../spring-boot-webflux")
    public static HttpService springWebFlux = new HttpService().setAutoStart(false);

    public static class SpringWebFluxState extends ServiceState<HttpService> {

        public SpringWebFluxState() {
            super(springWebFlux);
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

    @Benchmark
    public String springWebFlux(SpringWebFluxState state) {
        return runBenchmark(state);
    }

    private String runBenchmark(ServiceState<HttpService> state) {
        HttpResponse<String> response = state.getService().getString("/ping");
        assertEquals("pong", response.body());
        return response.body();
    }
}
