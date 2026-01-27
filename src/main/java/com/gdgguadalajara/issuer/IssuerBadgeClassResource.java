package com.gdgguadalajara.issuer;

import java.util.UUID;

import com.gdgguadalajara.badgeclass.application.CreateBadgeClass;
import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.badgeclass.model.dto.CreateBadgeClassRequest;
import com.gdgguadalajara.membership.model.MemberRole;
import com.gdgguadalajara.security.annotations.OrgRole;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@Path("/api/v2/issuers/{issuerUuid}/badges")
@RequiredArgsConstructor
public class IssuerBadgeClassResource {

    private final CreateBadgeClass createBadgeClass;

    @POST
    @Authenticated
    @OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })
    public BadgeClass create(UUID issuerUuid, CreateBadgeClassRequest request) {
        return createBadgeClass.run(issuerUuid, request);
    }
}
