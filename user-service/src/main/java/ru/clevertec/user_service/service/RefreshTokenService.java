package ru.clevertec.user_service.service;

import ru.clevertec.user_service.dao.RefreshTokenRepository;
import ru.clevertec.user_service.exception.InvalidTokenException;
import ru.clevertec.user_service.model.RefreshToken;
import ru.clevertec.user_service.model.User;
import ru.clevertec.user_service.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenRepository repository;

    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                refreshToken = authHeader.substring("Bearer ".length());
                checkToken(refreshToken);
                username = jwtService.extractUsername(refreshToken);
                User user = userService.findByUsername(username);
                saveOldToken(refreshToken, user);
                if (jwtService.isTokenValid(refreshToken, user)) {
                    String accessToken = jwtService.generateAccessToken(user);
                    String newRefreshToken = jwtService.generateRefreshToken(user);
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("accessToken", accessToken);
                    tokens.put("refreshToken", newRefreshToken);
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

    private void checkToken(String refreshToken) {
        Optional<RefreshToken> checkedToken = repository.findByToken(refreshToken);
        if (checkedToken.isPresent()){
            throw new InvalidTokenException("Refresh token has been used");
        }
    }

    private void saveOldToken(String refreshToken, User user) {
        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expirationTime(LocalDateTime.now())
                .build();
        repository.save(token);
    }
}
