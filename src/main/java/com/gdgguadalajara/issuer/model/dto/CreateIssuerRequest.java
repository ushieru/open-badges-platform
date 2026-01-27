package com.gdgguadalajara.issuer.model.dto;

public record CreateIssuerRequest(
        String name,
        String description,
        String url,
        String email,
        String logoUrl) {

}
