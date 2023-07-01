package ru.clevertec.news_service.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.news_service.config.Config;
import ru.clevertec.news_service.integration.BaseIntegrationTest;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Config.class})
class NewsCommentControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void checkFindShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/full/find")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty();
    }

    @Test
    void checkFindNewsListShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/full/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty();
    }

    @Test
    void checkFindNewsPageShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/full/news/page?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.allElements").isNumber()
                .jsonPath("$.allPages").isNumber();
    }

    @Test
    void checkFindCommentsListShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/full/comments?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty();
    }

    @Test
    void checkFindCommentsPageShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/full/comments/page?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.allElements").isNumber()
                .jsonPath("$.allPages").isNumber();
    }
}
