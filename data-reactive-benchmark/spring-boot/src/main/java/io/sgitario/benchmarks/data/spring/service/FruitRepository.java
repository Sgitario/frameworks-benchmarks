package io.sgitario.benchmarks.data.spring.service;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FruitRepository extends ReactiveCrudRepository<Fruit, Long> {
}
