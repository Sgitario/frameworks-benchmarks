package io.sgitario.benchmarks.data.quarkus.service;

import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/api/fruits")
public class FruitResource {

    private final FruitRepository repository;

    public FruitResource(FruitRepository repository) {
        this.repository = repository;
    }

    @GET
    @Path("/{id}")
    public Fruit get(@PathParam("id") Long id) {
        return repository.findById(id);
    }

    @POST
    @Transactional
    public Response post(Fruit fruit) {
        repository.persist(fruit);
        return Response
                .status(Response.Status.CREATED)
                .entity(fruit)
                .build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        repository.deleteById(id);
        return Response.noContent().build();
    }

}
