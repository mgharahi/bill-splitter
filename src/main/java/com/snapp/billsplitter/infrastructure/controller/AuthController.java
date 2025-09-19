package com.snapp.billsplitter.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {


    @PostMapping("/token")
    public ResponseEntity<?> getToken() {

        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> revokeToken() {

        return null;
    }
}