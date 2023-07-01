package ru.clevertec.news_service.integration.controller;

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
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.AddNewsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsPageDtoBuilder;
import ru.clevertec.news_service.builder.dto.UpdatedNewsDtoBuilder;
import ru.clevertec.news_service.config.Config;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.integration.BaseIntegrationTest;
import ru.clevertec.news_service.model.News;

import java.util.Collections;
import java.util.List;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Config.class})
class NewsControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static NewsDto newsDto;
    private static News news;
    private static NewsDto updatedNewsDto;
    private static NewsPageDto newsPageDto;
    private static AddNewsDto addNewsDto;
    private static List<News> listNews;
    private static List<NewsDto> dtoListNews;

    @BeforeEach
    void setUp() {
        TestBuilder<NewsDto> newsDtoBuilder = new NewsDtoBuilder();
        newsDto = newsDtoBuilder.build();
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        TestBuilder<NewsPageDto> newsPageDtoBuilder = new NewsPageDtoBuilder();
        newsPageDto = newsPageDtoBuilder.build();
        TestBuilder<AddNewsDto> addNewsDtoBuilder = new AddNewsDtoBuilder();
        addNewsDto = addNewsDtoBuilder.build();
        TestBuilder<NewsDto> updatedNewsDtoBuilder = new UpdatedNewsDtoBuilder();
        updatedNewsDto = updatedNewsDtoBuilder.build();
        listNews = Collections.singletonList(news);
        dtoListNews = Collections.singletonList(newsDto);
    }

    @Test
    void checkFindAllShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = 1L)
    void checkFindByIdShouldReturnExpectedComment(Long id) {
        webTestClient.get()
                .uri("/api/v1/news/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("journalist")
                .jsonPath("$.text").isEqualTo("Scientists have discovered a new planet outside our solar system...");
    }

    @Test
    void checkFindPageShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/news/page?page=0&size=10")
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
    @Transactional
    void checkAddShouldSaveNewEntityAndReturnStatus200() {
        webTestClient.post()
                .uri("/api/v1/news")
                .body(Mono.just(addNewsDto), AddNewsDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 5L)
    void checkUpdateShouldUpdateEntityAndReturnStatus200(Long id) {
        webTestClient.put()
                .uri("/api/v1/news/update/" + id)
                .body(Mono.just(addNewsDto), AddNewsDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("journalist")
                .jsonPath("$.text").isEqualTo("this is a text")
                .jsonPath("$.title").isEqualTo("Breaking news");
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 10L)
    void checkDeleteByIdShouldReturnStatus204(Long id) {
        webTestClient.delete()
                .uri("/api/v1/news/delete/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }
}