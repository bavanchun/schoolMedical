package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.AuthRequest;
import com.schoolhealth.schoolmedical.model.dto.request.RegisterRequest;
import com.schoolhealth.schoolmedical.model.dto.response.AuthenticationResponse;
import com.schoolhealth.schoolmedical.service.authenticate.AuthenticateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication REST API Endpoints", description = "Operations related to register & login")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticateService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
