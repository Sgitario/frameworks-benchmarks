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
import io.jcloud.api.Scenario;
import io.jcloud.core.ServiceState;

@Scenario
@Fork(3)
@Warmup(iterations = 1)
@Measurement(iterations = 3)
@BenchmarkMode(Mode.Throughput)
@Threads(50)
public class ThroughputBenchmarks extends RestBenchmarks {

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
