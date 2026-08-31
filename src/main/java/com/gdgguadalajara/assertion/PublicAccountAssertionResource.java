package com.gdgguadalajara.assertion;

import java.util.UUID;

import com.gdgguadalajara.assertion.application.ListPublicAssertions;
import com.gdgguadalajara.assertion.model.dto.PublicAssertionResponse;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;

import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;

@Path("/api/v2/accounts/{accountUuid}/assertions")
@AllArgsConstructor
public class PublicAccountAssertionResource {

    private final ListPublicAssertions listPublicAssertions;

    @GET
    public PaginatedResponse<PublicAssertionResponse> read(UUID accountUuid,
            @BeanParam @Valid PaginationRequestParams params) {
        return listPublicAssertions.run(accountUuid, params);
    }
}