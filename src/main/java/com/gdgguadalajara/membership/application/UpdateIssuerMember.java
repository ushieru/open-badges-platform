package com.gdgguadalajara.membership.application;

import java.util.UUID;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.authentication.application.GetCurrentSession;
import com.gdgguadalajara.common.model.DomainException;
import com.gdgguadalajara.issuer.model.Issuer;
import com.gdgguadalajara.membership.model.IssuerMember;
import com.gdgguadalajara.membership.model.MemberRole;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UpdateIssuerMember {

    private final GetCurrentSession getCurrentSession;

    public IssuerMember run(UUID issuerUuid, UUID accountUuid, MemberRole role) {
        var session = getCurrentSession.run();
        if (!session.isSuperAdmin)
            throw DomainException.forbidden("Solo super administradores pueden crear miembros de emisores");
        var issuer = Issuer.<Issuer>findById(issuerUuid);
        if (issuer == null)
            throw DomainException.notFound("Emisor no encontrado");
        var account = Account.<Account>findById(accountUuid);
        if (account == null)
            throw DomainException.notFound("Cuenta no encontrada");
        var member = IssuerMember.<IssuerMember>find("issuer = ?1 and account = ?2", issuer, account).firstResult();
        if (member == null)
            throw DomainException.notFound("Miembro no encontrado");
        return run(member, role);
    }

    @Transactional
    public IssuerMember run(IssuerMember member, MemberRole role) {
        member.role = role;
        member.persistAndFlush();
        return member;
    }
}
