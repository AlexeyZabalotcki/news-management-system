package ru.clevertec.news_service.util;

import com.netflix.discovery.EurekaClient;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.clevertec.news_service.dto.UserDto;

import java.util.Objects;

@Slf4j
@Service

@RequiredArgsConstructor
public class CommunicationService {

    private final WebClient webClient;
    private final EurekaClient eurekaClient;

    private static final String USER_CHECK_URL = "/gateway/check/{username}";

    public String getUsernameFromToken(String token) {
        String jwt = token.replace("Bearer ", "");
        String jwtWithoutSignature = jwt.substring(0, jwt.lastIndexOf('.') + 1);
        String username;
        try {
            username = Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(jwtWithoutSignature)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            throw new ValidationException("Token has expired!");
        }
        return username;
    }

    public String findByUsername(String username) {
        String userMsHomePage = eurekaClient.getNextServerFromEureka("user-service", false).getHomePageUrl();
        return Objects.requireNonNull(webClient.get()
                .uri(userMsHomePage + USER_CHECK_URL, username)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block())
                .getUsername();
    }
}
