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
import ru.clevertec.news_service.builder.CommentBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.AddCommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.CommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.CommentPageDtoBuilder;
import ru.clevertec.news_service.builder.dto.UpdatedCommentDtoBuilder;
import ru.clevertec.news_service.config.Config;
import ru.clevertec.news_service.dto.AddCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.integration.BaseIntegrationTest;
import ru.clevertec.news_service.model.Comment;

import java.util.Collections;
import java.util.List;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Config.class})
class CommentControllerTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static CommentDto commentDto;
    private static Comment comment;
    private static CommentPageDto commentPageDto;
    private static AddCommentDto addCommentDto;
    private static CommentDto updatedCommentDto;
    private static List<Comment> comments;
    private static List<CommentDto> commentsDto;

    @BeforeEach
    void setUp() {
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<Comment> commentTestBuilder = new CommentBuilder();
        comment = commentTestBuilder.build();
        TestBuilder<CommentPageDto> commentPageDtoTestBuilder = new CommentPageDtoBuilder();
        commentPageDto = commentPageDtoTestBuilder.build();
        comments = Collections.singletonList(comment);
        commentsDto = Collections.singletonList(commentDto);
        TestBuilder<AddCommentDto> addCommentDtoTestBuilder = new AddCommentDtoBuilder();
        addCommentDto = addCommentDtoTestBuilder.build();
        TestBuilder<CommentDto> updateCommentDtoTestBuilder = new UpdatedCommentDtoBuilder();
        updatedCommentDto = updateCommentDtoTestBuilder.build();
    }

    @Test
    void checkFindAllShouldReturn200() {
        webTestClient.get()
                .uri("/api/v1/comment")
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
                .uri("/api/v1/comment/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("User 2")
                .jsonPath("$.text").isEqualTo("Comment 1 for news 1");
    }

    @ParameterizedTest
    @ValueSource(longs = 5L)
    void checkFindAllByNewsIdShouldReturn200(Long id) {
        webTestClient.get()
                .uri("/api/v1/comment/" + id + "/comments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = 5L)
    void checkFindPageByNewsIdShouldReturn200(Long id) {
        webTestClient.get()
                .uri("/api/v1/comment/" + id + "/comments/page?page=0&size=10")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.allElements").isNumber()
                .jsonPath("$.allPages").isNumber();
    }

    @ParameterizedTest
    @Transactional
    @ValueSource(longs = 5L)
    void checkAddShouldSaveNewEntityAndReturnStatus200(Long id) {
        webTestClient.post()
                .uri("/api/v1/comment/save/" + id)
                .body(Mono.just(addCommentDto), AddCommentDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 5L)
    void checkAddShouldUpdateEntityAndReturnStatus200(Long id) {
        webTestClient.put()
                .uri("/api/v1/comment/update/" + id)
                .body(Mono.just(addCommentDto), AddCommentDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("subscriber")
                .jsonPath("$.text").isEqualTo("This is a comment for news!");
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 10L)
    void checkDeleteByIdShouldReturnStatus204(Long id) {
        webTestClient.delete()
                .uri("/api/v1/comment/delete/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = 7L)
    void checkDeleteAllByNewsIdShouldReturn204(Long id) {
        webTestClient.delete()
                .uri("/api/v1/comment/" + id + "/comments")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }
}