package com.gdgguadalajara.issuer.application;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.assertion.model.AssertionStatus;
import com.gdgguadalajara.assertion.model.dto.AssertionFilterParams;
import com.gdgguadalajara.assertion.model.dto.BadgeAssertionItem;
import com.gdgguadalajara.assertion.model.dto.BadgeAssertionRecipient;
import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.common.model.DomainException;
import com.gdgguadalajara.common.model.PaginatedResponse;
import com.gdgguadalajara.common.model.dto.PaginationRequestParams;
import com.gdgguadalajara.common.utils.PanacheCriteria;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ListBadgeAssertions {

    @Transactional
    public PaginatedResponse<BadgeAssertionItem> run(UUID issuerUuid, UUID badgeClassUuid,
            AssertionFilterParams filters, PaginationRequestParams params) {
        BadgeClass badge = BadgeClass.<BadgeClass>findById(badgeClassUuid);
        if (badge == null || !badge.issuer.id.equals(issuerUuid))
            throw DomainException.notFound("Credencial no encontrada");

        var criteria = PanacheCriteria.<Assertion>of(Assertion.class)
                .eq("badgeClass.id", badgeClassUuid);

        if (filters.status != null)
            applyStatus(criteria, filters.status);

        if (filters.from != null)
            criteria.ge("issuedOn", toInstantStart(filters.from));
        if (filters.to != null)
            criteria.le("issuedOn", toInstantEnd(filters.to));

        if (filters.search != null && !filters.search.isBlank())
            criteria.like("recipientEmail", filters.search);

        var page = criteria.orderBy(params.sortOrDefault("issuedOn,desc"))
                .page(params.page, params.size)
                .getResult();

        List<BadgeAssertionItem> items = new ArrayList<>();
        for (Assertion assertion : page.data)
            items.add(toItem(assertion));

        return new PaginatedResponse<>(items, page.meta);
    }

    private void applyStatus(PanacheCriteria<Assertion> criteria, AssertionStatus status) {
        switch (status) {
            case CLAIMED -> criteria.isNotNull("account").eq("isRevoked", false);
            case PENDING -> criteria.isNull("account");
            case REVOKED -> criteria.eq("isRevoked", true);
        }
    }

    private Instant toInstantStart(LocalDate date) {
        return date.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    private Instant toInstantEnd(LocalDate date) {
        return date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
    }

    private BadgeAssertionItem toItem(Assertion assertion) {
        AssertionStatus status;
        if (assertion.isRevoked)
            status = AssertionStatus.REVOKED;
        else if (assertion.account != null)
            status = AssertionStatus.CLAIMED;
        else
            status = AssertionStatus.PENDING;

        BadgeAssertionRecipient recipient;
        if (assertion.account != null)
            recipient = new BadgeAssertionRecipient(assertion.account.fullName, assertion.account.email);
        else
            recipient = new BadgeAssertionRecipient(null, null);

        return new BadgeAssertionItem(
                assertion.id, recipient, status, assertion.issuedOn, assertion.evidence, assertion.isPublic);
    }
}