package com.gdgguadalajara.authentication.application;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.common.model.DomainException;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class GetCurrentSession {

    private final SecurityIdentity identity;

    public Account run() {
        var email = identity.getPrincipal().getName();
        var account = Account.<Account>find("email", email).firstResult();
        if (account == null)
            throw DomainException.notFound("Cuenta no encontrada");
        return account;
    }
}
