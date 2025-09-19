package com.snapp.billsplitter.infrastructure.service.auth;

public interface RevocationToken {
    void revokeAccess(String token);
    boolean isRevoked(String token);
}
