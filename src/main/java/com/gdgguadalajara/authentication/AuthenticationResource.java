package com.gdgguadalajara.authentication;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.common.model.DomainException;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;

@Path("/api/auth")
@AllArgsConstructor
public class AuthenticationResource {

    private final SecurityIdentity identity;

    @GET
    @Path("/me")
    @Authenticated
    public Account login() {
        var email = identity.getPrincipal().getName();
        var account = Account.<Account>find("email", email).firstResult();
        if (account == null)
            throw DomainException.badRequest("No account found for the authenticated user");
        return account;
    }
}
