package com.gdgguadalajara.account.model.dto;

import java.util.UUID;

public record PublicProfileResponse(
        UUID accountUuid,
        String fullName,
        Long totalPublic) {

}