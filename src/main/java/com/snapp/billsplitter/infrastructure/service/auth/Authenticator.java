package com.snapp.billsplitter.infrastructure.service.auth;


import com.snapp.billsplitter.infrastructure.service.auth.dto.Credential;
import com.snapp.billsplitter.infrastructure.service.auth.dto.TokenPackage;

public interface Authenticator {
    TokenPackage getToken(Credential credential);
}