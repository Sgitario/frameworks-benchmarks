package io.sgitario.benchmarks.data.quarkus.service;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

@ApplicationScoped
public class FruitRepository implements PanacheRepositoryBase<Fruit, Long> {
}
