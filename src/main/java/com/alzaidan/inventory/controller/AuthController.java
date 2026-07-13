package com.alzaidan.inventory.controller;

import com.alzaidan.inventory.dto.AuthDto;
import com.alzaidan.inventory.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Login and token management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
        summary = "Login",
        description = "Authenticates a user with username and password. Returns a Bearer JWT token valid for 24 hours."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful — token returned"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password"),
        @ApiResponse(responseCode = "400", description = "Validation error — missing fields")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthDto.LoginResponse> login(
            @Valid @RequestBody AuthDto.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}