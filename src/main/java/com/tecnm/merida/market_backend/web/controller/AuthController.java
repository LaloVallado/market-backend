package com.tecnm.merida.market_backend.web.controller;

import com.tecnm.merida.market_backend.domain.dto.LoginRequestDTO;
import com.tecnm.merida.market_backend.domain.dto.LoginResponseDTO;
import com.tecnm.merida.market_backend.domain.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Login and JWT token generation")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Validates the client's email and password (BCrypt) and returns a JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Example Login",
                                    value = """
                                            {
                                              "email": "lionel@messi.com",
                                              "password": "1234"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponse(responseCode = "200", description = "Login successful, returns the JWT token")
    @ApiResponse(responseCode = "401", description = "Invalid email or password")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}