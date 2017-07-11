package com.enya.jee.test.web;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.concurrent.Callable;

/**
 * Created by Enya on 7/11/2017.
 */
public class UserResourceClient {

    public Callable<Response> getUser() {
        return (Callable) () -> ClientBuilder.newClient().target("http://localhost:8080/web/user").request().get();
    }

    public Callable<Response> addUser() {
        return (Callable) () -> ClientBuilder.newClient().target("http://localhost:8080/web/user/add")
                .queryParam("name", "Enya1")
                .queryParam("age", 23)
                .request().get();
    }

    public Callable<Response> getAll() {
        return (Callable) () -> ClientBuilder.newClient().target("http://localhost:8080/web/user/all").request().get();
    }

}
