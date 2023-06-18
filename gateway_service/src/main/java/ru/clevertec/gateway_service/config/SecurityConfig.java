package ru.clevertec.gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
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
    private static final String SUBSCRIBER_ROLE = "SUBSCRIBER";
    private static final String JOURNALIST_ROLE = "JOURNALIST";

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



        http.authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/authenticate/auth")
                .permitAll()
                .pathMatchers("/users/**",
                        "/comment/**")
                .hasAuthority(ADMIN_ROLE)
                .pathMatchers("/news/**")
                .hasAnyAuthority(ADMIN_ROLE, JOURNALIST_ROLE)
                .pathMatchers("/full/**")
                .hasAnyAuthority(ADMIN_ROLE, JOURNALIST_ROLE, SUBSCRIBER_ROLE)
                .pathMatchers("/authenticate/**")
                .permitAll()
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository);

        return http.build();
    }
}
