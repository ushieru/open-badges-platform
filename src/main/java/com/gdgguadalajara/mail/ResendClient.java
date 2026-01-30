package com.gdgguadalajara.mail;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.gdgguadalajara.mail.model.dto.ResendRequest;

import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/emails/batch")
@RegisterRestClient(configKey = "resend-api")
public interface ResendClient {

    @POST
    void sendEmail(
            @HeaderParam("Authorization") String authorization,
            List<ResendRequest> request);
}