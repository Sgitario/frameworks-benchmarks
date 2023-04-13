package io.sgitario.benchmarks.data.quarkus.service;

import org.jboss.resteasy.reactive.ResponseStatus;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/api/fruits")
public class FruitResource {

    private final FruitRepository repository;

    public FruitResource(FruitRepository repository) {
        this.repository = repository;
    }

    @GET
    @WithSession
    @Path("/{id}")
    public Uni<Fruit> get(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    @WithTransaction
    @ResponseStatus(201)
    public Uni<Fruit> post(Fruit fruit) {
        return repository.persist(fruit);
    }

    @DELETE
    @WithTransaction
    @Path("/{id}")
    @ResponseStatus(204)
    public Uni<Boolean> delete(@PathParam("id") Long id) {
        return repository.deleteById(id);
    }

}
