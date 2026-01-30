package com.gdgguadalajara.mail.model.dto;

import com.gdgguadalajara.assertion.model.Assertion;

public record SendBadgeMailNotificationRequest(
        Assertion assertion,
        String email) {

}
