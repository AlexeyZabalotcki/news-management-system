package ru.clevertec.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final ReactiveAuthenticationManager authenticationManager;
    private final ServerSecurityContextRepository securityContextRepository;
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String JOURNALIST_ROLE = "JOURNALIST";
    private static final String SUBSCRIBER_ROLE = "SUBSCRIBER";

    public SecurityConfig(ReactiveAuthenticationManager authenticationManager, ServerSecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http = http.cors().and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .formLogin()
                .disable();


        http
                .authorizeExchange()
                .pathMatchers("/authenticate/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/news/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/comment/**")
                .permitAll()
                .pathMatchers(HttpMethod.GET, "/full/**")
                .permitAll()
                .pathMatchers("/users/**")
                .hasAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.POST,"/news/**")
                .hasAnyAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.PUT,"/news/**")
                .hasAnyAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.DELETE,"/news/**")
                .hasAnyAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.POST,"/comment/**")
                .hasAnyAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.PUT,"/comment/**")
                .hasAnyAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.DELETE,"/comment/**")
                .hasAnyAuthority(ADMIN_ROLE)
                .pathMatchers(HttpMethod.POST, "/comments/**")
                .hasAnyAuthority(ADMIN_ROLE, JOURNALIST_ROLE, SUBSCRIBER_ROLE)
                .pathMatchers(HttpMethod.PUT, "/comments/**")
                .hasAnyAuthority(ADMIN_ROLE, JOURNALIST_ROLE, SUBSCRIBER_ROLE)
                .pathMatchers(HttpMethod.DELETE, "/comments/**")
                .hasAnyAuthority(ADMIN_ROLE, JOURNALIST_ROLE, SUBSCRIBER_ROLE)
                .pathMatchers("/journalist/**")
                .hasAnyAuthority(ADMIN_ROLE, JOURNALIST_ROLE)
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository);

        return http.build();
    }

}
