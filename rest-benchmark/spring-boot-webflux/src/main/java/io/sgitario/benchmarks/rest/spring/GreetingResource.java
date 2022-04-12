package io.sgitario.benchmarks.rest.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingResource {

    @GetMapping("/ping")
    public String pong() {
        return "pong";
    }
}
