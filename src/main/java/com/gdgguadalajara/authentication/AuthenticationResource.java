package com.gdgguadalajara.authentication;

import com.gdgguadalajara.authentication.application.Authentication;
import com.gdgguadalajara.authentication.model.dto.AuthenticationRequest;
import com.gdgguadalajara.authentication.model.dto.AuthenticationResponse;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;

@Path("/api/auth")
@AllArgsConstructor
public class AuthenticationResource {
    
    private final Authentication authentication;

    @POST
    public AuthenticationResponse auth(AuthenticationRequest request) {
        return authentication.run(request);
    }
}
