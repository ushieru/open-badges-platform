package com.gdgguadalajara.assertion.model.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateAssertionVisibilityRequest(
        @NotNull Boolean isPublic) {

}