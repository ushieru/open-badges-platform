package com.gdgguadalajara.assertion.application;

import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.Hibernate;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.assertion.model.dto.MyAssertionResponse;
import com.gdgguadalajara.assertion.model.dto.UpdateAssertionVisibilityRequest;
import com.gdgguadalajara.authentication.application.GetCurrentSession;
import com.gdgguadalajara.common.model.DomainException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional
public class UpdateAssertionVisibility {

    private final GetCurrentSession getCurrentSession;

    @ConfigProperty(name = "com.gdgguadalajara.open-badges-platform.domain")
    public String domain;

    public MyAssertionResponse run(UUID assertionUuid, UpdateAssertionVisibilityRequest request) {
        var account = getCurrentSession.run();
        var assertion = Assertion.<Assertion>find("id = ?1 and account = ?2", assertionUuid, account).firstResult();
        if (assertion == null)
            throw DomainException.notFound("Acreditación no encontrada");

        if (Boolean.TRUE.equals(request.isPublic())) {
            if (Boolean.TRUE.equals(assertion.isRevoked))
                throw DomainException.badRequest("La acreditación está revocada");
            if (assertion.htmlPayload == null || assertion.htmlPayload.isBlank()) {
                Hibernate.initialize(assertion.badgeClass);
                Hibernate.initialize(assertion.badgeClass.issuer);
                Hibernate.initialize(assertion.badgeClass.image);
                assertion.htmlPayload = CreateAssertion.Templates.htmlPayload(domain, assertion).render()
                        .replaceAll("(?s)", "")
                        .replaceAll("(?s)\\s+", " ")
                        .replaceAll("> <", "><")
                        .replaceAll("\\s+\\{", "{")
                        .replaceAll("\\{\\s+", "{")
                        .replaceAll(";\\s+", ";")
                        .trim();
            }
        }
        assertion.isPublic = request.isPublic();
        assertion.persistAndFlush();

        var image = assertion.badgeClass.image;
        var imageUrl = image == null ? null : String.format("%s/api/storage/images/%s", domain, image.id);
        return new MyAssertionResponse(
                assertion.id,
                assertion.badgeClass.name,
                imageUrl,
                assertion.issuedOn,
                assertion.isPublic,
                assertion.isRevoked,
                String.format("%s/api/v2/assertions/%s", domain, assertion.id));
    }
}