package com.gdgguadalajara.issuer;

import java.util.List;
import java.util.UUID;

import com.gdgguadalajara.assertion.application.CreateAssertion;
import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.assertion.model.dto.EmitBadgeRequest;
import com.gdgguadalajara.membership.model.MemberRole;
import com.gdgguadalajara.security.annotations.OrgRole;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@Path("/api/v2/issuers/{issuerUuid}/badges/{badgeClassUuid}/assertions")
@RequiredArgsConstructor
public class IssuerAssertionResource {

    private final CreateAssertion createAssertion;

    @POST
    @Authenticated
    @OrgRole({ MemberRole.OWNER })
    public List<Assertion> create(UUID issuerUuid, UUID badgeClassUuid, EmitBadgeRequest request) {
        return createAssertion.run(badgeClassUuid, request);
    }

}
