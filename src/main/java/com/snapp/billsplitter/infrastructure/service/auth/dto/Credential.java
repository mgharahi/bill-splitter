package com.snapp.billsplitter.infrastructure.service.auth.dto;

public record Credential(String username, String password, String refreshToken) {
}