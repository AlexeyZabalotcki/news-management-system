package ru.clevertec.gateway_service.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.news.AddNewsDto;
import ru.clevertec.gateway_service.dto.news.NewsDto;
import ru.clevertec.gateway_service.dto.news.NewsPageDto;
import ru.clevertec.gateway_service.feign_client.NewsMsReactiveFeignClient;

import java.util.List;


@Tag(description = "To interact with news", name = "News controller")
@RestController
@RequestMapping("/openapi/news")
public class NewsOpenApiDocumentationController {

    private final NewsMsReactiveFeignClient newsMsReactiveFeignClient;

    public NewsOpenApiDocumentationController(NewsMsReactiveFeignClient newsMsReactiveFeignClient) {
        this.newsMsReactiveFeignClient = newsMsReactiveFeignClient;
    }

    @Operation(summary = "Get all news")
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
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<List<NewsDto>> getAll() {
        return newsMsReactiveFeignClient.findAllNews();
    }

    @Operation(summary = "Get news by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Not found",
                               "status": 404,
                               "detail": "Something not found",
                               "instance": "/path"
                            }"""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/news/some",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<NewsDto> findById(@Parameter(description = "news id", required = true, example = "1")
                                                  @PathVariable Long id) {
       return newsMsReactiveFeignClient.findNewsById(id);
    }

    @Operation(summary = "Get news page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Not found",
                               "status": 404,
                               "detail": "Something not found",
                               "instance": "/path"
                            }"""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/path",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<NewsPageDto> findPageNews(@RequestParam(defaultValue = "0", required = false) Integer page,
                                          @RequestParam(defaultValue = "1", required = false) Integer size) {
        return newsMsReactiveFeignClient.findPageNews(page, size);
    }

    @Operation(summary = "Creation of new news",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddNewsDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Bad request",
                               "status": 400,
                               "detail": "Something bad",
                               "instance": "/path"
                            }"""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/path",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<NewsDto> addNews(@RequestBody AddNewsDto news) {
        return newsMsReactiveFeignClient.addNews(news);
    }

    @Operation(summary = "Updating of news",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddNewsDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Bad request",
                               "status": 400,
                               "detail": "Something bad",
                               "instance": "/path"
                            }"""))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Not found",
                               "status": 404,
                               "detail": "Something not found",
                               "instance": "/path"
                            }"""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/path",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<NewsDto> update(@Parameter(description = "News id", required = true, example = "1")
                                    @PathVariable Long id,
                                @RequestBody AddNewsDto updatedNews) {
        return newsMsReactiveFeignClient.updateNews(id, updatedNews);
    }

    @Operation(summary = "Delete news by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Bad request",
                               "status": 400,
                               "detail": "Something bad",
                               "instance": "/path"
                            }"""))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(example = """
                            {
                              "timestamp": "2023-06-22T19:57:49.870+00:00",
                              "path": "/path",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @DeleteMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<NewsDto> deleteById(@Parameter(description = "News id", required = true, example = "1")
                                                    @RequestParam("id") Long id) {
        return newsMsReactiveFeignClient.deleteNewsById(id);
    }

}
