package com.gdgguadalajara.issuer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdgguadalajara.issuer.model.Issuer;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record IssuerJsonLd(
        @JsonProperty("@context") String context,
        String type,
        String id,
        String name,
        String url,
        String email,
        String description,
        String image,
        String revocationList) {
    public IssuerJsonLd {
        if (context == null)
            context = "https://w3id.org/openbadges/v2";
        if (type == null)
            type = "Issuer";
    }

    public static IssuerJsonLd fromEntity(String baseUrl, Issuer issuer) {
        var canonicalId = String.format("%s/api/v2/issuers/%s", baseUrl, issuer.id);
        
        return new IssuerJsonLd(
            "https://w3id.org/openbadges/v2",
            "Issuer",
            canonicalId,
            issuer.name,
            issuer.url,
            issuer.email,
            issuer.description,
            issuer.logoUrl,
            canonicalId + "/revocations"
        );
    }
}
