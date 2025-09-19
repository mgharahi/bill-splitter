package com.snapp.billsplitter.infrastructure.service.auth.dto;

public record TokenPackage(String accessToken, String refreshToken) {
}