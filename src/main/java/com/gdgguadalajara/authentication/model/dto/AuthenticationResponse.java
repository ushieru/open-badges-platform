package com.gdgguadalajara.authentication.model.dto;

import com.gdgguadalajara.account.model.Account;

public record AuthenticationResponse(
        String token,
        Account account) {
}
