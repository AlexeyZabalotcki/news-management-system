package ru.clevertec.user_service.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.user_service.annotation.ClearDb;
import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.builder.UserBuilder;
import ru.clevertec.user_service.config.Config;
import ru.clevertec.user_service.model.User;

@ClearDb
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Config.class})
class GatewayControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static User user;

    @BeforeEach
    void setUp() {
        TestBuilder<User> userBuilder = new UserBuilder();
        user = userBuilder.build();
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(strings = "admin")
    void checkUserByUsername(String email) {
        webTestClient.get()
                .uri("gateway/check/" + email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("admin");
    }
}
