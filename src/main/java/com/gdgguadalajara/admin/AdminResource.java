package com.gdgguadalajara.admin;

import java.util.UUID;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.authentication.application.GetCurrentSession;
import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.common.PageBuilder;
import com.gdgguadalajara.common.model.DomainException;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;
import com.gdgguadalajara.issuer.model.Issuer;
import com.gdgguadalajara.membership.model.IssuerMember;

import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;

@Path("/api/admin")
@AllArgsConstructor
public class AdminResource {

    private final GetCurrentSession getCurrentSession;

    @GET
    @Path("/issuers/{uuid}")
    public Issuer readIssuerByUuid(UUID uuid) {
        var issuer = Issuer.<Issuer>findById(uuid);
        if (issuer == null)
            throw DomainException.notFound("Emisor no encontrado");
        return issuer;
    }

    @GET
    @Path("/badges/{uuid}")
    public BadgeClass readBadgeByUuid(UUID uuid) {
        var baadge = BadgeClass.<BadgeClass>findById(uuid);
        if (baadge == null)
            throw DomainException.notFound("Credencial no encontrada");
        return baadge;
    }

    @GET
    @Path("/me/assertions")
    @Authenticated
    public PaginatedResponse<Assertion> readAssertions(@BeanParam @Valid PaginationRequestParams request) {
        var account = getCurrentSession.run();
        return PageBuilder.of(Assertion.find("account", account), request);
    }

    @GET
    @Path("/assertions/{assertionUuid}")
    @Authenticated
    public Assertion readAssertionByUuid(UUID assertionUuid) {
        var account = getCurrentSession.run();
        var assertion = Assertion.<Assertion>findById(assertionUuid);
        if (assertion == null)
            throw DomainException.notFound("Acreditación no encontrada");
        if (account.isSuperAdmin)
            return assertion;
        if (assertion.account.id.equals(account.id))
            return assertion;
        var counter = IssuerMember.find(
                "account.id = ?1 and issuer.id = ?2",
                account.id,
                assertion.badgeClass.issuer.id).count();
        if (counter > 0)
            return assertion;
        throw DomainException.notFound("Acreditación no encontrada");
    }
}
