package ru.clevertec.user_service.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.clevertec.user_service.annotation.ClearDb;
import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.builder.dto.InputUserDtoBuilder;
import ru.clevertec.user_service.builder.dto.InputUserDtoBuilderForIntegration;
import ru.clevertec.user_service.builder.dto.UserDtoBuilder;
import ru.clevertec.user_service.config.Config;
import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.dto.UserDto;

@ClearDb
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Config.class})
public class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static UserDto userDto;
    private static InputUserDto inputUserDtoForSave;
    private static InputUserDto inputUserDtoForUpdate;

    @BeforeEach
    void setUp() {
        TestBuilder<UserDto> userDtoBuilder = new UserDtoBuilder();
        userDto = userDtoBuilder.build();
        TestBuilder<InputUserDto> userDtoBuilderForSave = new InputUserDtoBuilderForIntegration();
        inputUserDtoForSave = userDtoBuilderForSave.build();
        TestBuilder<InputUserDto> userDtoBuilderForUpdate = new InputUserDtoBuilder();
        inputUserDtoForUpdate = userDtoBuilderForUpdate.build();

    }

    @Test
    void checkGetAllShouldReturnStatus200() {
        webTestClient.get()
                .uri("/api/v1/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    void checkFindByIdShouldReturnExpectedUser(Long id) {
        webTestClient.get()
                .uri("/api/v1/users/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("admin");
    }

    @ParameterizedTest
    @ValueSource(longs = 7L)
    void checkFindByIdShouldReturnStatus404(Long id) {
        webTestClient.get()
                .uri("/api/v1/users/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 1L)
    void checkUpdateShouldUpdateEntityAndReturnStatus200(Long id) {
        webTestClient.put()
                .uri("/api/v1/users/update/" + id)
                .body(Mono.just(inputUserDtoForUpdate), InputUserDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("new name");
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 2L)
    void checkDeleteByIdShouldReturnStatus204(Long id) {
        webTestClient.delete()
                .uri("/api/v1/users/delete/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }
}
