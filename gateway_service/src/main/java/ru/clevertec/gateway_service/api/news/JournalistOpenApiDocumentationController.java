package ru.clevertec.gateway_service.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.news.AddNewsDto;
import ru.clevertec.gateway_service.dto.news.NewsDto;
import ru.clevertec.gateway_service.feign_client.NewsMsReactiveFeignClient;


@Tag(description = "To interact with news by Journalist", name = "JournalistNews controller")
@RestController
@RequestMapping("/openapi/journalist")
public class JournalistOpenApiDocumentationController {

    private final NewsMsReactiveFeignClient newsMsReactiveFeignClient;

    public JournalistOpenApiDocumentationController(NewsMsReactiveFeignClient newsMsReactiveFeignClient) {
        this.newsMsReactiveFeignClient = newsMsReactiveFeignClient;
    }

    @Operation(summary = "Creation of new news by Journalist",
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
    @PostMapping("add")
    @PreAuthorize("hasAuthority('JOURNALIST')")
    public Mono<NewsDto> addNews(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                                 @Valid @RequestBody AddNewsDto news) {
        return newsMsReactiveFeignClient.addNewsByJournalist(authorization, news);
    }

    @Operation(summary = "Updating of news by Journalist",
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
    @PreAuthorize("hasAuthority('JOURNALIST')")
    public Mono<NewsDto> update(@Parameter(description = "News id", required = true, example = "1")
                                @PathVariable Long id,
                                @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                                @RequestBody AddNewsDto updatedNews) {
        return newsMsReactiveFeignClient.updateNewsByJournalist(authorization, id, updatedNews);
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
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('JOURNALIST')")
    public Mono<NewsDto> deleteById(@Parameter(description = "News id", required = true, example = "1")
                                    @PathVariable("id") Long id,
                                    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        return newsMsReactiveFeignClient.deleteNewsByJournalist(authorization, id);
    }

}
