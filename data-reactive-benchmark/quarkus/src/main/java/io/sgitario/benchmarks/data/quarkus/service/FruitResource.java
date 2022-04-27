package io.sgitario.benchmarks.data.quarkus.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.context.ThreadContext;
import org.jboss.resteasy.reactive.ResponseStatus;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.context.api.CurrentThreadContext;
import io.smallrye.mutiny.Uni;

@Path("/api/fruits")
public class FruitResource {

    private final FruitRepository repository;

    public FruitResource(FruitRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("/{id}")
    @CurrentThreadContext(propagated = {}, cleared = {}, unchanged = ThreadContext.ALL_REMAINING)
    public Uni<Fruit> get(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    @ReactiveTransactional
    @ResponseStatus(201)
    @CurrentThreadContext(propagated = {}, cleared = {}, unchanged = ThreadContext.ALL_REMAINING)
    public Uni<Fruit> post(Fruit fruit) {
        return repository.persist(fruit);
    }

    @DELETE
    @ReactiveTransactional
    @Path("/{id}")
    @ResponseStatus(204)
    @CurrentThreadContext(propagated = {}, cleared = {}, unchanged = ThreadContext.ALL_REMAINING)
    public Uni<Boolean> delete(@PathParam("id") Long id) {
        return repository.deleteById(id);
    }

}
