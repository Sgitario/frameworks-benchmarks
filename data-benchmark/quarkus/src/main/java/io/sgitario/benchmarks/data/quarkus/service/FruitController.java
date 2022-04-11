package io.sgitario.benchmarks.data.quarkus.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.reactive.ResponseStatus;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@Path("/api/fruits")
public class FruitController {

    private final FruitRepository repository;

    public FruitController(FruitRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("/{id}")
    public Uni<Fruit> get(@PathParam("id") Integer id) {
        return repository.findById(id);
    }

    @GET
    public Uni<List<Fruit>> getAll() {
        return repository.findAll().list();
    }

    @ResponseStatus(201)
    @POST
    @ReactiveTransactional
    public Uni<Fruit> post(Fruit fruit) {
        return repository.persist(fruit);
    }

    @ResponseStatus(204)
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Integer id) {
        repository.deleteById(id);
    }

}
