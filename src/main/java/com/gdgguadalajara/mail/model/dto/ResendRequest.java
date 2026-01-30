package com.gdgguadalajara.mail.model.dto;

public record ResendRequest(
        String from,
        String to,
        String subject,
        String html) {
}