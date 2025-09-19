package com.snapp.billsplitter.infrastructure.util;

import com.snapp.billsplitter.infrastructure.config.property.AuthorizationProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser {

    private final String username;
    private final String accessToken;
    private final Long userId;


    public CurrentUser(HttpServletRequest request, AuthorizationProperties authorizationProperties,
                       JwtUtil jwtUtil) {

        String header = request.getHeader(authorizationProperties.header().name());
        if (header != null && header.startsWith(authorizationProperties.token().prefix())) {
            accessToken = header.substring(7);
            this.userId = jwtUtil.getUserIdFromToken(accessToken);
        } else {
            accessToken = null;
            userId = null;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!Objects.isNull(auth) && auth.isAuthenticated()) {
            this.username = auth.getName();
        } else {
            this.username = null;
        }
    }
}