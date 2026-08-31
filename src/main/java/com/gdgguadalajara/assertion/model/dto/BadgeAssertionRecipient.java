package com.gdgguadalajara.assertion.model.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record BadgeAssertionRecipient(
        String fullName,
        String email) {
}