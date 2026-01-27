package com.gdgguadalajara.badgeclass.application;

import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.badgeclass.model.dto.BadgeClassJsonLd;
import com.gdgguadalajara.badgeclass.model.dto.CreateBadgeClassRequest;
import com.gdgguadalajara.common.model.DomainException;
import com.gdgguadalajara.issuer.model.Issuer;
import com.gdgguadalajara.storage.application.CreateImage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CreateBadgeClass {

    private final CreateImage createImage;
    private final ObjectMapper objectMapper;

    @ConfigProperty(name = "com.gdgguadalajara.open-badges-platform.domain")
    public String domain;

    public BadgeClass run(UUID issuerUuid, CreateBadgeClassRequest request) {
        Issuer issuer = Issuer.<Issuer>findById(issuerUuid);
        if (issuer == null)
            throw DomainException.notFound("Emisores no encontrado");
        return run(issuer, request);
    }

    @Transactional
    public BadgeClass run(Issuer issuer, CreateBadgeClassRequest request) {
        var image = createImage.fromBase64(request.imageBase64());
        var badgeClass = new BadgeClass();
        badgeClass.name = request.name();
        badgeClass.description = request.description();
        badgeClass.criteriaMd = request.criteriaMd();
        badgeClass.image = image;
        badgeClass.issuer = issuer;
        badgeClass.persistAndFlush();
        var badgeClassJsonLD = BadgeClassJsonLd.fromEntity(domain, badgeClass);
        try {
            badgeClass.jsonPayload = objectMapper.writeValueAsString(badgeClassJsonLD);
            badgeClass.persistAndFlush();
        } catch (JsonProcessingException e) {
            throw DomainException.badRequest("Error al generar el JSON-LD del BadgeClass");
        }
        return badgeClass;
    }
}
