package com.gdgguadalajara.security;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.gdgguadalajara.account.model.Account;

import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AccountAugmentor implements SecurityIdentityAugmentor {

    @ConfigProperty(name = "com.gdgguadalajara.open-badges-platform.security.super-admins")
    Optional<List<String>> superAdmins;

    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
    if (identity.isAnonymous())
        return Uni.createFrom().item(identity);
    UserInfo userInfo = identity.getAttribute("userinfo");
    String email = null;
    String name = null;
    if (userInfo != null) {
        email = userInfo.getString("email");
        name = userInfo.getString("name");
    }     
    if (email == null)
        email = identity.getPrincipal().getName();
    if (email == null || email.isBlank()) {
        return Uni.createFrom().item(identity);
    }
    final String finalEmail = email;
    final String finalName = (name != null) ? name : email;
    return context.runBlocking(() -> {
        syncAccount(finalEmail, finalName);
        return identity;
    });
}

    @Transactional
    void syncAccount(String email, String name) {
        var account = Account.<Account>find("email", email)
                .firstResultOptional().orElseGet(() -> {
                    Account newAcc = new Account();
                    newAcc.email = email;
                    return newAcc;
                });
        account.fullName = name;
        var shouldBeAdmin = superAdmins
                .map(list -> list.contains(email))
                .orElse(false);
        account.isSuperAdmin = shouldBeAdmin;
        account.persist();
    }
}
