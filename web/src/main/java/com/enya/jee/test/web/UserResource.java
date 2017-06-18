package com.enya.jee.test.web;

import com.enya.jee.test.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @GET
    public Response getUser() {
        return Response.ok(userService.getUser()).build();
    }

    @GET
    @Path("add")
    public Response addUser(
            @QueryParam("name") final String name,
            @QueryParam("age") final int age
    ) {
        userService.addUser(name, age);
        return Response.accepted().build();
    }

    @GET
    @Path("all")
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }
}
