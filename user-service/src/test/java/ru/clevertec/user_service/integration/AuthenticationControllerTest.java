package ru.clevertec.user_service.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.clevertec.user_service.annotation.ClearDb;
import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.builder.dto.LoginDtoBuilder;
import ru.clevertec.user_service.builder.dto.RegisterDtoBuilderForIntegration;
import ru.clevertec.user_service.config.Config;
import ru.clevertec.user_service.dto.LoginDto;
import ru.clevertec.user_service.dto.RegisterDto;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ClearDb
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Config.class})
class AuthenticationControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static RegisterDto registerDto;
    private static LoginDto loginDto;

    @BeforeEach
    void setUp() {
        TestBuilder<RegisterDto> registerDtoBuilder = new RegisterDtoBuilderForIntegration();
        registerDto = registerDtoBuilder.build();
        TestBuilder<LoginDto> loginDtoBuilder = new LoginDtoBuilder();
        loginDto = loginDtoBuilder.build();
    }

    @Test
    void authenticate() {
        webTestClient.post()
                .uri("/api/v1/auth/auth")
                .body(Mono.just(loginDto), LoginDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accessToken").exists()
                .jsonPath("$.refreshToken").exists();
    }

    @Test
    void refreshToken() {
        byte[] refreshTokenBytes = webTestClient.post()
                .uri("/api/v1/auth/auth")
                .body(Mono.just(loginDto), LoginDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.refreshToken").exists()
                .returnResult().getResponseBody();

        String refreshAndAccessToken = new String(refreshTokenBytes, StandardCharsets.UTF_8);
        String refreshToken = extractRefreshToken(refreshAndAccessToken);

        webTestClient.get()
                .uri("/api/v1/auth/refresh")
                .header("Authorization", "Bearer " + refreshToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    private String extractRefreshToken(String response) {
        Pattern pattern = Pattern.compile("\"refreshToken\":\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Failed to extract refreshToken from response");
    }
}
