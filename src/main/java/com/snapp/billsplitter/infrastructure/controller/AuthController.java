package com.snapp.billsplitter.infrastructure.controller;



import com.snapp.billsplitter.infrastructure.controller.dto.AuthRequest;
import com.snapp.billsplitter.infrastructure.mapper.auth.AuthMapper;
import com.snapp.billsplitter.infrastructure.service.auth.AuthenticatorFactory;
import com.snapp.billsplitter.infrastructure.service.auth.PasswordAuthenticator;
import com.snapp.billsplitter.infrastructure.controller.dto.TokenPackage;
import com.snapp.billsplitter.infrastructure.service.messages.MessageHelper;
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

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@Valid @RequestBody AuthRequest request) {

        log.info("Received auth request {}", request);

        TokenPackage result = authMapper.toTokenPackage(authenticatorFactory.getAuthentication(request.grantType())
                .getToken(authMapper.toCredential(request)));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> revokeToken(@Valid @RequestBody TokenPackage tokenPackage) {
        passwordAuthenticator.logout(authMapper.toTokenPackage(tokenPackage));
        return ResponseEntity.ok(messageHelper.getMessage("message.auth.success.logout"));
    }
}