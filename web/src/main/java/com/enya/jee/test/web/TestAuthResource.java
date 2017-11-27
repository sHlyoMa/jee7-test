package com.enya.jee.test.web;

import com.enya.jee.test.service.TestService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestAuthResource {

    @Inject
    private TestService testService;
    @Context
    private HttpServletRequest sessionContext;

    @GET
    @Path("admin")
    @RolesAllowed("admin")
    public Response admin(){
        System.out.println("resource: " + sessionContext.getUserPrincipal());
        String admin = testService.admin();
        return Response.ok(admin).build();
    }

    @GET
    @Path("user")
    @RolesAllowed("user")
    public Response user(){
        System.out.println("resource: " + sessionContext.getUserPrincipal());
        String admin = testService.user();
        return Response.ok(admin).build();
    }

    @GET
    @Path("public")
    @PermitAll
    public Response pub(){
        System.out.println("resource: " + sessionContext.getUserPrincipal());
        String admin = testService.pub();
        return Response.ok(admin).build();
    }
}
