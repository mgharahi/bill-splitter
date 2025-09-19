package com.snapp.billsplitter.infrastructure.service.auth;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRevocation implements RevocationToken {

    private final Set<String> revocationSet = ConcurrentHashMap.newKeySet();

    @Override
    public void revokeAccess(String token) {
        revocationSet.add(token);
    }

    @Override
    public boolean isRevoked(String token) {
        return revocationSet.contains(token);
    }
}
