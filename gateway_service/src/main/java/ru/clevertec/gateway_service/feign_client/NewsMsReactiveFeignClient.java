package ru.clevertec.gateway_service.feign_client;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.news.AddCommentDto;
import ru.clevertec.gateway_service.dto.news.AddNewsDto;
import ru.clevertec.gateway_service.dto.news.AddUsersCommentDto;
import ru.clevertec.gateway_service.dto.news.CommentDto;
import ru.clevertec.gateway_service.dto.news.CommentPageDto;
import ru.clevertec.gateway_service.dto.news.NewsCommentsDto;
import ru.clevertec.gateway_service.dto.news.NewsDto;
import ru.clevertec.gateway_service.dto.news.NewsPageDto;

import java.util.List;

import static ru.clevertec.gateway_service.api.news.NewsMsConstants.NEWS_MS;

@Service
@ReactiveFeignClient(name = NEWS_MS)
public interface NewsMsReactiveFeignClient {

    @GetMapping("/api/v1/news")
    Mono<List<NewsDto>> findAllNews();

    @GetMapping("/api/v1/news/{id}")
    Mono<NewsDto> findNewsById(@PathVariable Long id);

    @GetMapping("/api/v1/news/page")
    Mono<NewsPageDto> findPageNews(@RequestParam(defaultValue = "0", required = false) Integer page,
                                   @RequestParam(defaultValue = "1", required = false) Integer size);

    @PostMapping("/api/v1/news")
    Mono<NewsDto> addNews(@RequestBody AddNewsDto news);

    @PutMapping("/api/v1/news/update/{id}")
    Mono<NewsDto> updateNews(@PathVariable Long id,
                             @RequestBody AddNewsDto updatedNews);

    @DeleteMapping("/api/v1/news/delete/{id}")
    Mono<NewsDto> deleteNewsById(@PathVariable Long id);

    @GetMapping("/api/v1/comment")
    Mono<List<CommentDto>> findAllComments();

    @GetMapping("/api/v1/comment/{id}")
    Mono<CommentDto> findCommentById(@PathVariable Long id);

    @GetMapping("/api/v1/comment/{id}/comments")
    Mono<List<CommentDto>> findAllCommentsByNewsId(@PathVariable Long id);

    @GetMapping("/api/v1/comment/{id}/comments/page")
    Mono<CommentPageDto> findCommentsPageByNewsId(@PathVariable Long id,
                                                  @RequestParam(defaultValue = "0", required = false) Integer page,
                                                  @RequestParam(defaultValue = "1", required = false) Integer size);

    @PostMapping("/api/v1/comment/save/{id}")
    Mono<CommentDto> addComment(@PathVariable Long id,
                                @Valid @RequestBody AddCommentDto commentDto);

    @PutMapping("/api/v1/comment/update/{id}")
    Mono<CommentDto> updateComment(@PathVariable Long id,
                                   @RequestBody AddCommentDto updatedComment);

    @DeleteMapping("/api/v1/comment/delete/{id}")
    Mono<CommentDto> deleteCommentById(@PathVariable Long id);

    @DeleteMapping("/api/v1/comment/{id}/comments")
    Mono<CommentDto> deleteCommentsByNewsId(@PathVariable Long id);

    @PostMapping("/api/v1/journalist/add")
    Mono<NewsDto> addNewsByJournalist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                      @Valid @RequestBody AddNewsDto news);

    @PutMapping("/api/v1/journalist/update/{id}")
    Mono<NewsDto> updateNewsByJournalist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @PathVariable Long id,
                                         @RequestBody AddNewsDto updatedNews);

    @DeleteMapping("/api/v1/journalist/delete/{id}")
    Mono<NewsDto> deleteNewsByJournalist(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @PathVariable Long id);

    @PostMapping("/api/v1/comments/add/{id}")
    Mono<CommentDto> addCommentsByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                       @PathVariable Long id,
                                       @Valid @RequestBody AddUsersCommentDto commentDto);

    @PutMapping("/api/v1/comments/update/{id}")
    Mono<CommentDto> updateCommentsByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                          @PathVariable Long id,
                                          @RequestBody AddUsersCommentDto updatedNews);

    @DeleteMapping("/api/v1/comments/delete/{id}")
    Mono<CommentDto> deleteCommentsByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                          @PathVariable Long id);

    @GetMapping("/api/v1/full/find")
    Mono<List<NewsCommentsDto>> findAllNewsWithComments(@RequestParam(required = false, defaultValue = "") String keyword);

    @GetMapping("/api/v1/full/news")
    Mono<List<NewsDto>> findAllNewsList(@RequestParam(required = false, defaultValue = "") String keyword);

    @GetMapping("/api/v1/full/news/page")
    Mono<NewsPageDto> findAllNewsPage(@RequestParam(required = false, defaultValue = "") String keywoed,
                                      @RequestParam(defaultValue = "0", required = false) Integer page,
                                      @RequestParam(defaultValue = "1", required = false) Integer size);

    @GetMapping("/api/v1/full/news")
    Mono<List<CommentDto>> findAllCommentsList(@RequestParam(required = false, defaultValue = "") String keyword);

    @GetMapping("/api/v1/full/comments/page")
    Mono<CommentPageDto> findAllCommentsPage(@RequestParam(required = false, defaultValue = "") String keyword,
                                             @RequestParam(defaultValue = "0", required = false) Integer page,
                                             @RequestParam(defaultValue = "1", required = false) Integer size);

}
