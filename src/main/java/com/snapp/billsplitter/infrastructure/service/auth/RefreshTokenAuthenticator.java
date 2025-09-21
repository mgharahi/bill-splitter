package com.snapp.billsplitter.infrastructure.service.auth;

import com.snapp.billsplitter.infrastructure.service.auth.dto.Credential;
import com.snapp.billsplitter.infrastructure.service.auth.dto.TokenPackage;
import com.snapp.billsplitter.infrastructure.util.MessageHelper;
import com.snapp.billsplitter.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RefreshTokenAuthenticator implements Authenticator {
    private final JwtUtil jwtUtil;
    private final RevocationToken blackList;
    private final MessageHelper messageHelper;
    private final UserDetailsService userDetailsService;

    @Override
    public TokenPackage getToken(Credential credential) {

        validateCredentials(credential);

        String oldRefreshToken = credential.refreshToken();

        String username = jwtUtil.getUsernameFromToken(oldRefreshToken);
        var userDetail = (UserDetails) userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.generateAccessToken(userDetail.getId(), username);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetail.getId(), username);

        blackList.revokeAccess(oldRefreshToken);

        return new TokenPackage(newAccessToken, newRefreshToken);
    }

    private void validateCredentials(Credential credential) {


        if (Objects.isNull(credential.refreshToken()) || credential.refreshToken().isBlank() || !jwtUtil.validateToken(credential.refreshToken()))
            throw new BadCredentialsException(messageHelper.getMessage("error.auth.bad.data.invalid.token"));

        if (blackList.isRevoked(credential.refreshToken()))
            throw new BadCredentialsException(messageHelper.getMessage("error.auth.bad.data.invalid.token"));
    }
}