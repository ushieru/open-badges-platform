package com.gdgguadalajara.security.filters;

import java.util.Arrays;
import java.util.UUID;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.membership.model.IssuerMember;
import com.gdgguadalajara.membership.model.MemberRole;
import com.gdgguadalajara.security.annotations.OrgRole;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

@OrgRole({})
@Provider
@Priority(Priorities.AUTHORIZATION)
@RequiredArgsConstructor
public class OrgSecurityFilter implements ContainerRequestFilter {

    private final SecurityIdentity identity;

    @Context
    ResourceInfo resourceInfo;

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var issuerParam = uriInfo.getPathParameters().getFirst("issuerUuid");
        if (issuerParam == null)
            return;
        var issuerId = UUID.fromString(issuerParam);
        var userEmail = identity.getPrincipal().getName();
        Account currentUser = Account.find("email", userEmail).firstResult();
        if (currentUser == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        var membership = IssuerMember.<IssuerMember>find(
                "account.id = ?1 and issuer.id = ?2",
                currentUser.id, issuerId).firstResult();
        if (membership == null || !isRoleAllowed(membership.role))
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"message\":\"No tienes permisos suficientes en esta organización.\"}")
                            .build());
    }

    private boolean isRoleAllowed(MemberRole userRole) {
        OrgRole annotation = resourceInfo.getResourceMethod().getAnnotation(OrgRole.class);
        if (annotation == null)
            annotation = resourceInfo.getResourceClass().getAnnotation(OrgRole.class);
        return annotation != null && Arrays.asList(annotation.value()).contains(userRole);
    }
}
