package com.gdgguadalajara.authentication.application;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.authentication.model.dto.AuthenticationRequest;
import com.gdgguadalajara.authentication.model.dto.AuthenticationResponse;
import com.gdgguadalajara.common.model.DomainException;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Authentication {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    public String issuer;

    @ConfigProperty(name = "mp.jwt.verify.audiences")
    public List<String> audiences;

    public AuthenticationResponse run(AuthenticationRequest request) {
        var account = Account.<Account>find("email", request.email()).firstResult();
        if (account == null)
            throw DomainException.badRequest("Error en credeniales");
        var isValid = BcryptUtil.matches(request.password(), account.password);
        if (!isValid)
            throw DomainException.badRequest("Error en credeniales");
        var token = Jwt
                .issuer(issuer)
                .audience(Set.copyOf(audiences))
                .subject(account.id.toString())
                .expiresIn(Duration.ofDays(7))
                .sign();
        return new AuthenticationResponse(token, account);
    }
}
