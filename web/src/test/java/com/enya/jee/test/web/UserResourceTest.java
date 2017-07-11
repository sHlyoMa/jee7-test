package com.enya.jee.test.web;

import com.enay.jee.test.model.User;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Enya on 7/11/2017.
 */
public class UserResourceTest {
    private ExecutorService es = Executors.newFixedThreadPool(1);

    @org.junit.Test
    public void getUser() throws Exception {
        Future<Response> e = es.submit(new UserResourceClient().getUser());
        System.out.println(e.get().readEntity(String.class));
        es.submit(new UserResourceClient().addUser());
        es.submit(new UserResourceClient().addUser());
        es.submit(new UserResourceClient().addUser());
        e = es.submit(new UserResourceClient().getUser());
        System.out.println(e.get().readEntity(String.class));
        e = es.submit(new UserResourceClient().getAll());
        System.out.println(e.get().readEntity(new GenericType<List<User>>(){}));
    }

    @org.junit.Test
    public void addUser() throws Exception {
    }

    @org.junit.Test
    public void getAllUsers() throws Exception {
    }

}
