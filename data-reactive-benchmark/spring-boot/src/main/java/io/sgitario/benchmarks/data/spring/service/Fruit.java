package io.sgitario.benchmarks.data.spring.service;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Fruit {

    @Id
    private Long id;

    private String name;

    public Fruit() {
    }

    public Fruit(String type) {
        this.name = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
