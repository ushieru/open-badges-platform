package com.gdgguadalajara.assertion.model.dto;

import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BadgeIssuanceSummary(
        UUID badgeId,
        String name,
        String imageUrl,
        long issued,
        long claimed,
        long pending,
        long revoked,
        double claimRate) {
}