package com.gdgguadalajara.assertion.model.dto;

import java.time.Instant;
import java.util.UUID;

public record MyAssertionResponse(
        UUID id,
        String badgeClassName,
        String badgeClassImageUrl,
        Instant issuedOn,
        Boolean isPublic,
        Boolean isRevoked,
        String url) {

}