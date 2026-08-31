package com.gdgguadalajara.account;

import java.util.UUID;

import com.gdgguadalajara.account.application.GetPublicProfile;
import com.gdgguadalajara.account.model.dto.PublicProfileResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;

@Path("/api/v2/accounts")
@AllArgsConstructor
public class PublicAccountResource {

    private final GetPublicProfile getPublicProfile;

    @GET
    @Path("/{accountUuid}")
    public PublicProfileResponse read(UUID accountUuid) {
        return getPublicProfile.run(accountUuid);
    }
}