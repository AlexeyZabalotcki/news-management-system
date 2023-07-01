package ru.clevertec.gateway_service.api.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import ru.clevertec.gateway_service.dto.news.AddCommentDto;
import ru.clevertec.gateway_service.dto.news.CommentDto;
import ru.clevertec.gateway_service.dto.news.CommentPageDto;
import ru.clevertec.gateway_service.feign_client.NewsMsReactiveFeignClient;

import java.util.List;


@Tag(description = "To interact with comments", name = "Comment controller")
@RestController
@RequestMapping("/openapi/comment")
public class CommentOpenApiDocumentationController {

    private final NewsMsReactiveFeignClient newsMsReactiveFeignClient;

    public CommentOpenApiDocumentationController(NewsMsReactiveFeignClient newsMsReactiveFeignClient) {
        this.newsMsReactiveFeignClient = newsMsReactiveFeignClient;
    }

    @Operation(summary = "Get all comments")
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
    public Mono<List<CommentDto>> getAll() {
        return newsMsReactiveFeignClient.findAllComments();
    }

    @Operation(summary = "Get comment by id")
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
    public Mono<CommentDto> findById(@Parameter(description = "comment id", required = true, example = "1")
                                     @PathVariable Long id) {
        return newsMsReactiveFeignClient.findCommentById(id);
    }

    @Operation(summary = "Get comments by news id")
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
    @GetMapping("/{id}/comments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<List<CommentDto>> findCommentsByNews(@Parameter(description = "news id", required = true, example = "1")
                                                     @PathVariable Long id) {
        return newsMsReactiveFeignClient.findAllCommentsByNewsId(id);
    }

    @Operation(summary = "Get comments page by news id")
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
    @GetMapping("/{id}/comments/page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<CommentPageDto> findCommentsPage(@Parameter(description = "news id", required = true, example = "1")
                                                 @PathVariable Long id,
                                                 @RequestParam(defaultValue = "0", required = false) Integer page,
                                                 @RequestParam(defaultValue = "1", required = false) Integer size) {
        return newsMsReactiveFeignClient.findCommentsPageByNewsId(id, page, size);
    }

    @Operation(summary = "Creation of new comment",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddCommentDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
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
    @PostMapping("/save/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<CommentDto> addComment(@Parameter(description = "news id", required = true, example = "1")
                                    @PathVariable Long id,
                                    @Valid @RequestBody AddCommentDto commentDto) {
        return newsMsReactiveFeignClient.addComment(id, commentDto);
    }

    @Operation(summary = "Updating of comment",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddCommentDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class))),
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
    public Mono<CommentDto> update(@Parameter(description = "Comment id", required = true, example = "1")
                                   @PathVariable Long id,
                                   @RequestBody AddCommentDto commentDto) {
        return newsMsReactiveFeignClient.updateComment(id, commentDto);
    }

    @Operation(summary = "Delete comment by id")
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
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<CommentDto> deleteById(@Parameter(description = "Comment id", required = true, example = "1")
                                       @PathVariable("id") Long id) {
        return newsMsReactiveFeignClient.deleteCommentById(id);
    }

    @Operation(summary = "Delete all comments by news id")
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
    @DeleteMapping("/{id}/comments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<CommentDto> deleteCommentsByNewsId(@Parameter(description = "News id", required = true, example = "1")
                                                   @PathVariable("id") Long id) {
        return newsMsReactiveFeignClient.deleteCommentsByNewsId(id);
    }

}
