package ru.clevertec.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.user.dto.AuthenticationResponseDto;
import ru.clevertec.user.dto.LoginDto;
import ru.clevertec.user.dto.RegisterDto;
import ru.clevertec.user.service.RefreshTokenService;
import ru.clevertec.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final RefreshTokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@Valid @RequestBody RegisterDto registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody LoginDto loginRequest) {
        return ResponseEntity.ok(userService.authenticate(loginRequest));
    }

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> tokenResponse = tokenService.refreshToken(request, response);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokenResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
