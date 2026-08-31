package com.gdgguadalajara.assertion.model.dto;

import java.time.Instant;
import java.util.UUID;

import com.gdgguadalajara.assertion.model.AssertionStatus;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BadgeAssertionItem(
        UUID assertionId,
        BadgeAssertionRecipient recipient,
        AssertionStatus status,
        Instant issuedOn,
        String evidence,
        boolean isPublic) {
}