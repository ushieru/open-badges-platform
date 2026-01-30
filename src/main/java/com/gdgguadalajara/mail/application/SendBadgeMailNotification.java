package com.gdgguadalajara.mail.application;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.gdgguadalajara.assertion.model.Assertion;
import com.gdgguadalajara.mail.ResendClient;
import com.gdgguadalajara.mail.model.dto.ResendRequest;
import com.gdgguadalajara.mail.model.dto.SendBadgeMailNotificationRequest;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SendBadgeMailNotification {

    @Inject
    @RestClient
    ResendClient resendClient;

    @ConfigProperty(name = "resend.from")
    String FROM;

    @ConfigProperty(name = "resend.api.key")
    String apiKey;

    @ConfigProperty(name = "com.gdgguadalajara.open-badges-platform.domain")
    public String domain;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance email(String baseUrl, Assertion assertion);
    }

    public void run(List<SendBadgeMailNotificationRequest> requests) {
        var sendEmailrequests = requests.stream().map(r -> new ResendRequest(
                FROM,
                r.email(),
                "Has obtenido una insignia de " + r.assertion().badgeClass.issuer.name,
                Templates.email(domain, r.assertion()).render()))
                .toList();

        try {
            resendClient.sendEmail("Bearer " + apiKey, sendEmailrequests);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
