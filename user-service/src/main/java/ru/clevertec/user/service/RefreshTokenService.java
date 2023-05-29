package ru.clevertec.user.service;

import ru.clevertec.user.model.User;
import ru.clevertec.user.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;
    private final UserService userService;

    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                refreshToken = authHeader.substring(7);
                username = jwtService.extractUsername(refreshToken);
                User user = userService.findByUsername(username);
                if (jwtService.isTokenValid(refreshToken, user)) {
                    String accessToken = jwtService.generateAccessToken(user);
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("refresh-token", refreshToken);
                    tokens.put("access-token", accessToken);
                    return tokens;
                }
            }
        } catch (Exception ex) {
            response.setHeader("error", ex.getMessage());
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error-message", ex.getMessage());
            return error;
        }
        return null;
    }
}
