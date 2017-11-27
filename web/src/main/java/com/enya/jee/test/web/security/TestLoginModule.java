package com.enya.jee.test.web.security;

import org.jboss.security.auth.spi.IdentityLoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class TestLoginModule extends IdentityLoginModule {
    private static final String PRINCIPAL = "principal";
    private static final String ROLES = "roles";

    @Override
    public void initialize(Subject subject, CallbackHandler handler, Map<String, ?> sharedState, Map<String, ?> options) {
        Map<String, Object> map = new HashMap<>();
        map.put(PRINCIPAL, "Enya-1");
        map.put(ROLES, "user");
        super.initialize(subject, handler, sharedState, map);
    }

    @Override
    public boolean login() throws LoginException {
        return super.login();
    }

    @Override
    public boolean logout() throws LoginException {

        return super.logout();
    }
}
