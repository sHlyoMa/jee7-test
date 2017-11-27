package com.enya.jee.test.web.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * link to examples: {https://github.com/javaee-samples/javaee7-samples/tree/master/jaspic}.
 */
public class TestSAM implements ServerAuthModule {

    private String loginContextName = null;

    public TestSAM(String loginContextName) {
        this.loginContextName = loginContextName;
    }

    private CallbackHandler handler;
    private Class<?>[] supportedMessageTypes = new Class[]{HttpServletRequest.class, HttpServletResponse.class};

    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
                           @SuppressWarnings("rawtypes") Map options) throws AuthException {
        this.handler = handler;
    }

    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
            throws AuthException {

        Callback[] callbacks;
        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();

        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {

            // ### If already authenticated before, continue this session

            // Execute protocol to signal container registered authentication session be used.
            callbacks = new Callback[]{new CallerPrincipalCallback(clientSubject, userPrincipal)};
        } else {

            String name, role;
            if (request.getRequestURI().contains("user")) {
                name = "Enya-user";
                role = "user";
            } else if (request.getRequestURI().contains("admin")) {
                name = "Enya-admin";
                role = "admin";
            } else {
                name = "anonim";
                role = "a";
            }
            if (name == null || role == null) {
                HttpServletResponse response = (HttpServletResponse) messageInfo.getResponseMessage();

                try {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return AuthStatus.SEND_FAILURE;
                } catch (IOException e) {
                    throw (AuthException) new AuthException().initCause(e);
                }
            }

//        if (request.getParameter("doLogin") != null) {

            // For the test perform a login by directly "returning" the details of the authenticated user.
            // Normally credentials would be checked and the details fetched from some repository

//            PasswordValidationCallback pwd = new PasswordValidationCallback(clientSubject, "Enauuu", "pass-test".toCharArray());
//            try {
//                handler.handle(new Callback[]{pwd});
//                pwd.clearPassword();
//            } catch (IOException | UnsupportedCallbackException e) {
//                System.out.println("error" + e.getMessage());
//            }
//            if (pwd.getResult()) {
//                System.out.println("fjdsjflasjdf");
//            }

            callbacks = new Callback[]{
                    // The name of the authenticated user
                    new CallerPrincipalCallback(clientSubject, name),
                    // the roles of the authenticated user
                    new GroupPrincipalCallback(clientSubject, new String[]{role})
            };
//        } else {
//
//            // The JASPIC protocol for "do nothing"
//            callbacks = new Callback[] { new CallerPrincipalCallback(clientSubject, (Principal) null) };
//        }
        }
        try {

            // Communicate the details of the authenticated user to the container. In many
            // cases the handler will just store the details and the container will actually handle
            // the login after we return from this method.
            handler.handle(callbacks);

        } catch (IOException | UnsupportedCallbackException e) {
            throw (AuthException) new AuthException().initCause(e);
        }
        //next line register session and for the next call request.getUserCaller() returns this Principal.
        messageInfo.getMap().put("javax.servlet.http.registerSession", Boolean.TRUE.toString());


        return AuthStatus.SUCCESS;
    }

    @Override
    public Class<?>[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {

    }
}
