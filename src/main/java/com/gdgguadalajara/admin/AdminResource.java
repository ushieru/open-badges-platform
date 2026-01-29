package com.gdgguadalajara.admin;

import java.util.UUID;

import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.common.model.DomainException;
import com.gdgguadalajara.issuer.model.Issuer;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;

@Path("/api/admin")
@AllArgsConstructor
public class AdminResource {

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
}
