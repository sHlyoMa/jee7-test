package com.enya.jee.test.web.security;

import org.jboss.security.auth.container.modules.AbstractServerAuthModule;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import java.util.HashMap;

public class TestServerAuthModule extends AbstractServerAuthModule {

    private String loginContextName = null;

    public TestServerAuthModule()
    {
        this.supportedTypes.add(Object.class);
    }

    public TestServerAuthModule(String loginContextName) {
        this();
        this.loginContextName = loginContextName;
    }

    @Override
    protected boolean validate(Subject clientSubject, MessageInfo messageInfo) throws AuthException {
        boolean result = false;
        System.out.println("TestServerAuthModule.validate() run");
        TestLoginModule lm = new TestLoginModule();
        lm.initialize(clientSubject, callbackHandler, new HashMap(), options);
        try {
            lm.login();
            lm.commit();
            return true;
        } catch (LoginException e) {
            e.printStackTrace();
        }
//        super.validateRequest(messageInfo,clientSubject, null);
        return result;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        System.out.println("TestServerAuthModule.secureResponse() run");
        TestLoginModule lm = new TestLoginModule();
        lm.initialize(serviceSubject, callbackHandler, new HashMap(), options);
        try {
            lm.login();
            lm.commit();
            return AuthStatus.SUCCESS;
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return AuthStatus.FAILURE;
    }
}
