package com.snapp.billsplitter.infrastructure.service.auth;

import com.snapp.billsplitter.infrastructure.service.auth.dto.Credential;
import com.snapp.billsplitter.infrastructure.service.auth.dto.TokenPackage;
import com.snapp.billsplitter.infrastructure.service.messages.MessageHelper;
import com.snapp.billsplitter.infrastructure.util.CurrentUser;
import com.snapp.billsplitter.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PasswordAuthenticator implements Authenticator {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CurrentUser currentUser;
    private final MessageHelper messageHelper;
    private final RevocationToken blackList;
    private final UserDetailsService userDetailsService;

    @Override
    public TokenPackage getToken(Credential credential) {
        validateCredentials(credential);

        try {
            var auth = new UsernamePasswordAuthenticationToken(credential.username(), credential.password());
            authenticationManager.authenticate(auth);
            UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(credential.username());

            String accessToken = jwtUtil.generateAccessToken(userDetails.getId(), credential.username());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getId(), credential.username());

            return new TokenPackage(accessToken, refreshToken);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public void logout(TokenPackage tokenPackage) {
        validateTokenPackage(tokenPackage);
        blackList.revokeAccess(tokenPackage.refreshToken());
        blackList.revokeAccess(currentUser.getAccessToken());
    }

    private void validateTokenPackage(TokenPackage tokenPackage) {
        if (Objects.isNull(tokenPackage))
            throwInvalidCredentials();

        if (!jwtUtil.validateToken(tokenPackage.refreshToken()))
            throwInvalidCredentials();

        if (!jwtUtil.validateToken(tokenPackage.accessToken()))
            throwInvalidCredentials();

        if (!jwtUtil.getUsernameFromToken(tokenPackage.refreshToken()).equals(currentUser.getUsername()))
            throw new AccessDeniedException(messageHelper.getMessage("error.auth.do.not.have.access"));
    }

    private void validateCredentials(Credential credential) {
        if (Objects.isNull(credential))
            throwInvalidCredentials();

        if (Objects.isNull(credential.password()) || credential.password().isBlank())
            throwInvalidCredentials();

        if (Objects.isNull(credential.username()) || credential.username().isBlank())
            throwInvalidCredentials();
    }

    private void throwInvalidCredentials() {
        throw new BadCredentialsException(messageHelper.getMessage("error.auth.bad.data.invalid.credential"));
    }
}