package com.snapp.billsplitter.infrastructure.controller.dto;


import com.snapp.billsplitter.infrastructure.service.auth.dto.GrantType;
import jakarta.validation.constraints.NotNull;

public record AuthRequest(
        String username,
        String password,
        String refreshToken,

        @NotNull(message = "Grant type is required")
        GrantType grantType
) {
}