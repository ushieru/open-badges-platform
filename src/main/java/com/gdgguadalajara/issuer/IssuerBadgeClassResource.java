package com.gdgguadalajara.issuer;

import java.util.UUID;

import com.gdgguadalajara.assertion.model.dto.BadgeIssuanceSummary;
import com.gdgguadalajara.badgeclass.application.CreateBadgeClass;
import com.gdgguadalajara.badgeclass.application.RemoveBadgeClass;
import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.badgeclass.model.dto.CreateBadgeClassRequest;
import com.gdgguadalajara.common.PageBuilder;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;
import com.gdgguadalajara.issuer.application.BuildBadgeIssuanceSummary;
import com.gdgguadalajara.membership.model.MemberRole;
import com.gdgguadalajara.security.annotations.OrgRole;

import io.quarkus.panache.common.Sort;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@Path("/api/v2/issuers/{issuerUuid}/badges")
@RequiredArgsConstructor
public class IssuerBadgeClassResource {

    private final CreateBadgeClass createBadgeClass;
    private final RemoveBadgeClass removeBadgeClass;
    private final BuildBadgeIssuanceSummary buildBadgeIssuanceSummary;

    @GET
    public PaginatedResponse<BadgeClass> read(UUID issuerUuid, @BeanParam @Valid PaginationRequestParams params) {
        return PageBuilder.of(BadgeClass.find("issuer.id", Sort.descending("createdAt"), issuerUuid), params);
    }

    @GET
    @Path("/analytics")
    @Authenticated
    @OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })
    public PaginatedResponse<BadgeIssuanceSummary> analytics(UUID issuerUuid,
            @BeanParam @Valid PaginationRequestParams params) {
        return buildBadgeIssuanceSummary.run(issuerUuid, params);
    }

    @POST
    @Authenticated
    @OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })
    public BadgeClass create(UUID issuerUuid, CreateBadgeClassRequest request) {
        return createBadgeClass.run(issuerUuid, request);
    }

    @DELETE
    @Path("/{uuid}")
    @Authenticated
    @OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })
    public void delete(UUID issuerUuid, UUID uuid) {
        removeBadgeClass.run(uuid);
    }
}