package com.gdgguadalajara.membership.application;

import java.util.UUID;

import com.gdgguadalajara.membership.model.IssuerMember;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RemoveIssuerMember {

    public void run(UUID issuerUuid, UUID accountUuid) {
        IssuerMember member = IssuerMember.find("issuer.id = ?1 and account.id = ?2", issuerUuid, accountUuid)
                .firstResult();
        if (member == null)
            return;
        run(member);
    }

    @Transactional
    public void run(IssuerMember member) {
        member.delete();
    }
}
