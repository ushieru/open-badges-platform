package com.gdgguadalajara.assertion;

import java.util.UUID;

import com.gdgguadalajara.assertion.application.ListMyAssertions;
import com.gdgguadalajara.assertion.application.UpdateAssertionVisibility;
import com.gdgguadalajara.assertion.model.dto.MyAssertionResponse;
import com.gdgguadalajara.assertion.model.dto.UpdateAssertionVisibilityRequest;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;

import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;

@Path("/api/me/assertions")
@Authenticated
@AllArgsConstructor
public class MyAssertionResource {

    private final ListMyAssertions listMyAssertions;
    private final UpdateAssertionVisibility updateAssertionVisibility;

    @GET
    public PaginatedResponse<MyAssertionResponse> read(@BeanParam @Valid PaginationRequestParams params,
            @QueryParam("isPublic") Boolean isPublic) {
        return listMyAssertions.run(params, isPublic);
    }

    @PUT
    @Path("/{uuid}/visibility")
    public MyAssertionResponse updateVisibility(UUID uuid, UpdateAssertionVisibilityRequest request) {
        return updateAssertionVisibility.run(uuid, request);
    }
}