package com.snapp.billsplitter.infrastructure.controller.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LogoutRequest(@NotNull @NotBlank String refreshToken) {
}
