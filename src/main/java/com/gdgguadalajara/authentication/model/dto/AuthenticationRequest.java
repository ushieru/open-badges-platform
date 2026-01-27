package com.gdgguadalajara.authentication.model.dto;

public record AuthenticationRequest(
        String email,
        String password) {
}
