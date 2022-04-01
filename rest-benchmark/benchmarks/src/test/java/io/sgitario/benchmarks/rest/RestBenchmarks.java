package io.sgitario.benchmarks.rest;

import io.jcloud.api.HttpService;
import io.jcloud.api.Quarkus;
import io.jcloud.api.Spring;
import io.jcloud.core.EnableBenchmark;
import io.jcloud.core.ServiceState;

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
}
