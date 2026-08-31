package com.gdgguadalajara.assertion.model.dto;

import java.time.Instant;
import java.util.UUID;

public record PublicAssertionResponse(
        UUID id,
        String badgeClassName,
        String badgeClassImageUrl,
        String issuerName,
        Instant issuedOn,
        String url) {

}