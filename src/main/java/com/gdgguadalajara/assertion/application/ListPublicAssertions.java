package com.gdgguadalajara.assertion.application;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.gdgguadalajara.account.model.Account;
import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.assertion.model.dto.PublicAssertionResponse;
import com.gdgguadalajara.common.PageBuilder;
import com.gdgguadalajara.common.model.DomainException;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ListPublicAssertions {

    @ConfigProperty(name = "com.gdgguadalajara.open-badges-platform.domain")
    public String domain;

    public PaginatedResponse<PublicAssertionResponse> run(UUID accountUuid, PaginationRequestParams params) {
        var account = Account.<Account>findById(accountUuid);
        if (account == null)
            throw DomainException.notFound("Perfil no encontrado");

        PanacheQuery<Assertion> query = Assertion.find("account = ?1 and isPublic = ?2 and isRevoked = ?3",
                Sort.descending("issuedOn"), account, true, false);
        var page = PageBuilder.of(query, params);
        List<PublicAssertionResponse> mapped = page.data.stream()
                .map(this::toResponse)
                .toList();
        return new PaginatedResponse<>(mapped, page.meta);
    }

    private PublicAssertionResponse toResponse(Assertion assertion) {
        var image = assertion.badgeClass.image;
        var imageUrl = image == null ? null : String.format("%s/api/storage/images/%s", domain, image.id);
        return new PublicAssertionResponse(
                assertion.id,
                assertion.badgeClass.name,
                imageUrl,
                assertion.badgeClass.issuer.name,
                assertion.issuedOn,
                String.format("%s/api/v2/assertions/%s", domain, assertion.id));
    }
}