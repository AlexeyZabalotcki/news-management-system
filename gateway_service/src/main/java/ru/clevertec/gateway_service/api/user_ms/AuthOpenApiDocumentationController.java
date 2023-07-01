package ru.clevertec.gateway_service.api.user_ms;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.user.AuthenticationResponseDto;
import ru.clevertec.gateway_service.dto.user.LoginDto;
import ru.clevertec.gateway_service.dto.user.RegisterDto;
import ru.clevertec.gateway_service.feign_client.UserMsReactiveFeignClient;

@Tag(description = "To login and register", name = "Auth controller")
@RestController
@RequestMapping("/openapi/auth")
public class AuthOpenApiDocumentationController {

    private final UserMsReactiveFeignClient authReactiveFeignClient;

    public AuthOpenApiDocumentationController(UserMsReactiveFeignClient authReactiveFeignClient) {
        this.authReactiveFeignClient = authReactiveFeignClient;
    }

    @Operation(summary = "Registration for new user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Bad Request",
                               "status": 400,
                               "detail": "Some bad request",
                               "instance": "/path"
                            }"""))),
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
    @SecurityRequirements
    @PostMapping("/registration")
    public Mono<AuthenticationResponseDto> registration(@RequestBody RegisterDto registerDto) {
        return authReactiveFeignClient.register(registerDto);
    }

    @Operation(summary = "Login",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDto.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Bad Request",
                               "status": 400,
                               "detail": "Some bad request",
                               "instance": "/path"
                            }"""))),
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
    @SecurityRequirements
    @PostMapping("/login")
    public Mono<AuthenticationResponseDto> login(@RequestBody LoginDto loginDto) {
        return authReactiveFeignClient.login(loginDto);
    }

    @Operation(summary = "Get new access and refresh token by old refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(schema = @Schema(example = """
                            {
                               "type": "about:blank",
                               "title": "Bad Request",
                               "status": 400,
                               "detail": "Some bad request",
                               "instance": "/path"
                            }"""))),
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
    @SecurityRequirements
    @GetMapping("/refresh")
    public Mono<String> refresh(@Parameter(hidden = true)
                                @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader) {
        return authReactiveFeignClient.refresh("Bearer " + authHeader);
    }

}
