package com.gdgguadalajara.authentication.application;

import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.common.model.DomainException;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class GetCurrentSession {

    private final JsonWebToken jwt;

    public Account run() {
        var account = Account.<Account>findById(UUID.fromString(jwt.getSubject()));
        if (account == null)
            throw DomainException.notFound("Cuenta no encontrada");
        return account;
    }
}
