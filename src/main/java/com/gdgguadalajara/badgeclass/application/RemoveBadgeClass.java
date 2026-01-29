package com.gdgguadalajara.badgeclass.application;

import java.util.UUID;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.badgeclass.model.BadgeClass;
import com.gdgguadalajara.common.model.DomainException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class RemoveBadgeClass {

    public void run(UUID uuid) {
        var badge = BadgeClass.<BadgeClass>findById(uuid);
        if (badge == null)
            return;
        var count = Assertion.count("badgeClass", badge);
        if (count < 0)
            throw DomainException.badRequest("No puedes eliminar una badge que ya ha sido emitida");
        run(badge);
    }

    @Transactional
    public void run(BadgeClass badge) {
        badge.delete();
    }
}
