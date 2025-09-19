package com.snapp.billsplitter.infrastructure.controller;

import com.snapp.billsplitter.infrastructure.controller.dto.auth.AuthRequest;
import com.snapp.billsplitter.infrastructure.mapper.auth.AuthMapper;
import com.snapp.billsplitter.infrastructure.service.auth.AuthenticatorFactory;
import com.snapp.billsplitter.infrastructure.service.auth.PasswordAuthenticator;
import com.snapp.billsplitter.infrastructure.controller.dto.auth.TokenPackage;
import com.snapp.billsplitter.infrastructure.service.messages.MessageHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticatorFactory authenticatorFactory;
    private final AuthMapper authMapper;
    private final PasswordAuthenticator passwordAuthenticator;
    private final MessageHelper messageHelper;

    @Operation(
            summary = "Get access and refresh tokens",
            description = "Authenticate a user using username/password or refresh token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Authentication request containing username/password or refresh token",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tokens returned successfully",
                            content = @Content(schema = @Schema(implementation = TokenPackage.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid username, password or token")
            }
    )
    @PostMapping("/token")
    public ResponseEntity<TokenPackage> getToken(@Valid @RequestBody AuthRequest request) {

        log.info("Received auth request {}", request);

        TokenPackage result = authMapper.toTokenPackage(authenticatorFactory.getAuthentication(request.grantType())
                .getToken(authMapper.toCredential(request)));

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Revoke access token",
            description = "Logout a user and invalidate the provided token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Token package containing access and refresh token to revoke",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TokenPackage.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token revoked successfully"),
                    @ApiResponse(responseCode = "403", description = "Don not have access"),
                    @ApiResponse(responseCode = "401", description = "Invalid username, password or token")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> revokeToken(@Valid @RequestBody TokenPackage tokenPackage) {
        passwordAuthenticator.logout(authMapper.toTokenPackage(tokenPackage));
        return ResponseEntity.ok(messageHelper.getMessage("message.auth.success.logout"));
    }
}