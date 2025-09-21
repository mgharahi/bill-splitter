package com.snapp.billsplitter.infrastructure.service.auth;

import com.snapp.billsplitter.infrastructure.service.auth.dto.GrantType;
import com.snapp.billsplitter.infrastructure.util.MessageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatorFactory {

    private final MessageHelper messageHelper;
    private final PasswordAuthenticator passwordAuthenticator;
    private final RefreshTokenAuthenticator refreshTokenAuthenticator;

    public Authenticator getAuthentication(GrantType grantType) {
        return switch (grantType) {
            case PASSWORD -> passwordAuthenticator;
            case REFRESH_TOKEN -> refreshTokenAuthenticator;
            case null, default ->
                    throw new BadCredentialsException(messageHelper.getMessage("error.auth.bad.data.grantType", new Object[]{grantType}));
        };
    }
}