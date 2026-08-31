package com.gdgguadalajara.assertion.application;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.assertion.model.dto.MyAssertionResponse;
import com.gdgguadalajara.authentication.application.GetCurrentSession;
import com.gdgguadalajara.common.PageBuilder;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ListMyAssertions {

    private final GetCurrentSession getCurrentSession;

    @ConfigProperty(name = "com.gdgguadalajara.open-badges-platform.domain")
    public String domain;

    public PaginatedResponse<MyAssertionResponse> run(PaginationRequestParams params, Boolean isPublic) {
        var account = getCurrentSession.run();
        PanacheQuery<Assertion> query;
        if (isPublic == null)
            query = Assertion.find("account = ?1", Sort.descending("issuedOn"), account);
        else
            query = Assertion.find("account = ?1 and isPublic = ?2", Sort.descending("issuedOn"), account, isPublic);
        var page = PageBuilder.of(query, params);
        List<MyAssertionResponse> mapped = page.data.stream()
                .map(this::toResponse)
                .toList();
        return new PaginatedResponse<>(mapped, page.meta);
    }

    private MyAssertionResponse toResponse(Assertion assertion) {
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