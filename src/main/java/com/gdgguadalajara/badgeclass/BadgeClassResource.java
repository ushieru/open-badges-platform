package com.gdgguadalajara.badgeclass;

import java.util.UUID;

import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.common.model.DomainException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v2/badges")
public class BadgeClassResource {

    @GET
    @Path("/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String jsondl(UUID uuid) {
        var badge = BadgeClass.<BadgeClass>findById(uuid);
        if (badge == null)
            throw DomainException.notFound("Badge no encontrada");
        return badge.jsonPayload;
    }
}
