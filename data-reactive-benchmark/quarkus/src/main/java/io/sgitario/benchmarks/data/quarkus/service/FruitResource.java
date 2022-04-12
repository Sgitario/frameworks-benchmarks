package io.sgitario.benchmarks.data.quarkus.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jboss.resteasy.reactive.ResponseStatus;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@Path("/api/fruits")
public class FruitResource {

    private final FruitRepository repository;

    public FruitResource(FruitRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("/{id}")
    public Uni<Fruit> get(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    @ReactiveTransactional
    @ResponseStatus(201)
    public Uni<Fruit> post(Fruit fruit) {
        return repository.persist(fruit);
    }

    @DELETE
    @ReactiveTransactional
    @Path("/{id}")
    @ResponseStatus(204)
    public Uni<Boolean> delete(@PathParam("id") Long id) {
        return repository.deleteById(id);
    }

}
