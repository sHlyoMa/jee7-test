package com.enya.jee.test.service;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless
public class TestService {

    @Resource
    private SessionContext sessionContext;

    @RolesAllowed("admin")
    public String admin(){
        System.out.println(sessionContext.getCallerPrincipal());
        return "admin";
    }
    @RolesAllowed("user")
    public String user(){
        System.out.println(sessionContext.getCallerPrincipal());
        return "user";
    }

    @PermitAll
    public String pub(){
        System.out.println(sessionContext.getCallerPrincipal());
        return "public";
    }
}
