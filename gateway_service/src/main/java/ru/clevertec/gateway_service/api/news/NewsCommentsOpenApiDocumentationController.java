package ru.clevertec.gateway_service.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.news.CommentDto;
import ru.clevertec.gateway_service.dto.news.CommentPageDto;
import ru.clevertec.gateway_service.dto.news.NewsCommentsDto;
import ru.clevertec.gateway_service.dto.news.NewsDto;
import ru.clevertec.gateway_service.dto.news.NewsPageDto;
import ru.clevertec.gateway_service.feign_client.NewsMsReactiveFeignClient;

import java.util.List;


@Tag(description = "To interact with news", name = "News controller")
@RestController
@RequestMapping("/openapi/full")
public class NewsCommentsOpenApiDocumentationController {

    private final NewsMsReactiveFeignClient newsMsReactiveFeignClient;

    public NewsCommentsOpenApiDocumentationController(NewsMsReactiveFeignClient newsMsReactiveFeignClient) {
        this.newsMsReactiveFeignClient = newsMsReactiveFeignClient;
    }

    @Operation(summary = "Get all news with comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/find/some",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/find")
    public Mono<List<NewsCommentsDto>> getAllNewsComments(@Parameter(description = "keyword", required = false, example = "")
                                              @RequestParam(required = false, defaultValue = "") String keyword) {
        return newsMsReactiveFeignClient.findAllNewsWithComments(keyword);
    }


    @Operation(summary = "Get all news ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/find/some",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/news")
    public Mono<List<NewsDto>> getAllNewsList(@Parameter(description = "keyword", required = false, example = "")
                                              @RequestParam(required = false, defaultValue = "") String keyword) {
        return newsMsReactiveFeignClient.findAllNewsList(keyword);
    }

    @Operation(summary = "Get all news with comments page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/find/some",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/news/page")
    public Mono<NewsPageDto> getAllNewsPage(@Parameter(description = "keyword", required = false, example = "")
                                              @RequestParam(required = false, defaultValue = "") String keyword,
                                              @Parameter(description = "page number", required = true, example = "1")
                                              @RequestParam(defaultValue = "0", required = false) Integer page,
                                              @Parameter(description = "elements amount", required = true, example = "10")
                                              @RequestParam(defaultValue = "1", required = false) Integer size) {
        return newsMsReactiveFeignClient.findAllNewsPage(keyword, page, size);
    }

    @Operation(summary = "Get all news with comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/find/some",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/comments")
    public Mono<List<CommentDto>> getAllCommentsList(@Parameter(description = "keyword", required = false, example = "")
                                              @RequestParam(required = false, defaultValue = "") String keyword) {
        return newsMsReactiveFeignClient.findAllCommentsList(keyword);
    }

    @Operation(summary = "Get all news with comments page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/find/some",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/comments/page")
    public Mono<CommentPageDto> getAllCommentsPage(@Parameter(description = "keyword", required = false, example = "")
                                            @RequestParam(required = false, defaultValue = "") String keyword,
                                                   @Parameter(description = "page number", required = true, example = "1")
                                            @RequestParam(defaultValue = "0", required = false) Integer page,
                                                   @Parameter(description = "elements amount", required = true, example = "10")
                                            @RequestParam(defaultValue = "1", required = false) Integer size) {
        return newsMsReactiveFeignClient.findAllCommentsPage(keyword, page, size);
    }

}
