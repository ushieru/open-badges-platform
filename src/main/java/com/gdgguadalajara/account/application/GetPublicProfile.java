package com.gdgguadalajara.account.application;

import java.util.UUID;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.account.model.dto.PublicProfileResponse;
import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.common.model.DomainException;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GetPublicProfile {

    public PublicProfileResponse run(UUID accountUuid) {
        var account = Account.<Account>findById(accountUuid);
        if (account == null)
            throw DomainException.notFound("Perfil no encontrado");
        var totalPublic = Assertion.count("account = ?1 and isPublic = ?2 and isRevoked = ?3",
                account, true, false);
        return new PublicProfileResponse(accountUuid, account.fullName, totalPublic);
    }
}