package ru.clevertec.gateway_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().servers(List.of(new Server()
                        .url("http://{host}:{port}")
                        .description("custom server")
                        .variables(new ServerVariables()
                                .addServerVariable("host",new ServerVariable()
                                        .description("host name")
                                        ._default("localhost"))
                                .addServerVariable("port",new ServerVariable()
                                        .description("port value")
                                        ._default("8080")))))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .description("Provide JWT token to authenticate")
                                .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi.builder()
                .addOpenApiCustomizer(openApiCustomizerUser())
                .group("user-service")
                .pathsToMatch("/openapi/auth/**", "/openapi/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi newsOpenApi() {
        return GroupedOpenApi.builder()
                .addOpenApiCustomizer(openApiCustomizerNews())
                .group("news-system")
                .pathsToMatch("/openapi/news/**", "/openapi/comment/**",
                        "/openapi/comments/**", "/openapi/full/**", "/openapi/journalist/**")
                .build();
    }

    private OpenApiCustomizer openApiCustomizerUser() {
        return openApi -> openApi.info(new Info()
                .title("User API")
                .description("API for interacting with users")
                .version("1.0"));
    }

    private OpenApiCustomizer openApiCustomizerNews() {
        return openApi -> openApi.info(new Info()
                .title("News API")
                .description("API for interacting with news")
                .version("1.0"));
    }
}
