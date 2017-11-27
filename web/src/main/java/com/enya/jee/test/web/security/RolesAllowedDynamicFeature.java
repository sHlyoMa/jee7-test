package com.enya.jee.test.web.security;


import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * A {@link DynamicFeature} supporting the {@code javax.annotation.security.RolesAllowed},
 * {@code javax.annotation.security.PermitAll} and {@code javax.annotation.security.DenyAll}
 * on resource methods and sub-resource methods.
 * <p/>
 * The {@link javax.ws.rs.core.SecurityContext} is utilized, using the
 * {@link javax.ws.rs.core.SecurityContext#isUserInRole(String) } method,
 * to ascertain if the user is in one
 * of the roles declared in by a {@code &#64;RolesAllowed}. If a user is in none of
 * the declared roles then a 403 (Forbidden) response is returned.
 * <p/>
 * If the {@code &#64;DenyAll} annotation is declared then a 403 (Forbidden) response
 * is returned.
 * <p/>
 * If the {@code &#64;PermitAll} annotation is declared and is not overridden then
 * this filter will not be applied.
 * <p/>
 * If a user is not authenticated and annotated method is restricted for certain roles then a 403
 * (Not Authenticated) response is returned.
 *
 * @author Paul Sandoz
 * @author Martin Matula
 */
@Provider
public class RolesAllowedDynamicFeature implements DynamicFeature,Feature {

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext configuration) {
        // DenyAll on the method take precedence over RolesAllowed and PermitAll
        if (resourceInfo.getResourceMethod().isAnnotationPresent(DenyAll.class)) {
            configuration.register(new RolesAllowedRequestFilter());
            return;
        }

        // RolesAllowed on the method takes precedence over PermitAll
        RolesAllowed ra = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);
        if (ra != null) {
            configuration.register(new RolesAllowedRequestFilter(ra.value()));
            return;
        }

        // PermitAll takes precedence over RolesAllowed on the class
        if (resourceInfo.getResourceMethod().isAnnotationPresent(PermitAll.class)) {
            // Do nothing.
            return;
        }

        // DenyAll can't be attached to classes

        // RolesAllowed on the class takes precedence over PermitAll
        ra = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
        if (ra != null) {
            configuration.register(new RolesAllowedRequestFilter(ra.value()));
        }
    }

    @Override
    public boolean configure(FeatureContext context) {
        return true;
    }

    @Priority(Priorities.AUTHORIZATION) // authorization filter - should go after any authentication filters
    private static class RolesAllowedRequestFilter implements ContainerRequestFilter {

        private final boolean denyAll;
        private final String[] rolesAllowed;

        RolesAllowedRequestFilter() {
            this.denyAll = true;
            this.rolesAllowed = null;
        }

        RolesAllowedRequestFilter(final String[] rolesAllowed) {
            this.denyAll = false;
            this.rolesAllowed = (rolesAllowed != null) ? rolesAllowed : new String[] {};
        }

        @Override
        public void filter(final ContainerRequestContext requestContext) throws IOException {
            if (!denyAll) {
                if (rolesAllowed.length > 0 && !isAuthenticated(requestContext)) {
                    throw new ForbiddenException("User not authorized");
                }

                for (final String role : rolesAllowed) {
                    if (requestContext.getSecurityContext().isUserInRole(role)) {
                        return;
                    }
                }
            }

            throw new ForbiddenException("User not authorized");
        }

        private static boolean isAuthenticated(final ContainerRequestContext requestContext) {
            return requestContext.getSecurityContext().getUserPrincipal() != null;
        }
    }
}