package com.snapp.billsplitter.infrastructure.filter;

import com.snapp.billsplitter.infrastructure.config.property.AuthorizationProperties;
import com.snapp.billsplitter.infrastructure.service.auth.RevocationToken;
import com.snapp.billsplitter.infrastructure.util.MessageHelper;
import com.snapp.billsplitter.infrastructure.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RevocationToken blackList;
    private final MessageHelper messageHelper;
    private final AuthorizationProperties authorizationProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(authorizationProperties.header().name());
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith(authorizationProperties.token().prefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        if (blackList.isRevoked(jwt))
            throw new BadCredentialsException(messageHelper.getMessage("error.auth.bad.data.invalid.token"));
        try {
            username = jwtUtil.getUsernameFromToken(jwt);
        } catch (Exception e) {
            throw new BadCredentialsException(messageHelper.getMessage("error.auth.bad.data.invalid.token"));
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }


        filterChain.doFilter(request, response);
    }
}