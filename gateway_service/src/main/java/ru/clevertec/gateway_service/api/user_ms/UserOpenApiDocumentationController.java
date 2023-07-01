package ru.clevertec.gateway_service.api.user_ms;

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
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.user.InputUserDto;
import ru.clevertec.gateway_service.dto.user.UserDto;
import ru.clevertec.gateway_service.feign_client.UserMsReactiveFeignClient;

import java.util.List;


@Tag(description = "To interact with users", name = "Users controller")
@RestController
@RequestMapping("/openapi/users")
public class UserOpenApiDocumentationController {

    private final UserMsReactiveFeignClient userMsReactiveFeignClient;

    public UserOpenApiDocumentationController(UserMsReactiveFeignClient userMsReactiveFeignClient) {
        this.userMsReactiveFeignClient = userMsReactiveFeignClient;
    }

    @Operation(summary = "Get all users")
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
                              "path": "/find/alex123",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<List<UserDto>> getAll() {
        return userMsReactiveFeignClient.getAll();
    }

    @Operation(summary = "Get user by id")
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
                              "path": "/find/alex123",
                              "status": 500,
                              "error": "Internal Server Error",
                              "requestId": "1a921d74-14"
                            }""")))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<UserDto> findById(@Parameter(description = "user id", required = true, example = "1")
                                  @PathVariable Long id) {
        return userMsReactiveFeignClient.findById(id);
    }

    @Operation(summary = "Creation of new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = InputUserDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
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
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<UserDto> save(@RequestBody InputUserDto user) {
        return userMsReactiveFeignClient.save(user);
    }

    @Operation(summary = "Updating of user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = InputUserDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
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
    public Mono<UserDto> update(@Parameter(description = "User id", required = true, example = "1")
                                @PathVariable Long id,
                                @RequestBody InputUserDto updates) {
        return userMsReactiveFeignClient.update(id, updates);
    }

    @Operation(summary = "Delete user by id")
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
    public Mono<UserDto> deleteById(@Parameter(description = "User id", required = true, example = "1")
                                    @PathVariable("id") Long id) {
        return userMsReactiveFeignClient.deleteById(id);
    }
}
