package ru.clevertec.gateway_service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final ReactiveUserDetailsService userDetailsService;

    public AuthenticationManager(JwtTokenProvider jwtTokenProvider, ReactiveUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("authenticate {} ", authentication);
        String token = (String) authentication.getCredentials();

        log.info("token {} ", token);
        if (jwtTokenProvider.validate(token)) {
            String username = jwtTokenProvider.extractUsername(token);
            log.info("token username {} ", username);
            return userDetailsService.findByUsername(username)
                    .flatMap(userDetails -> {
                        var auth = new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                        return Mono.just(auth);
                    });
        } else {
            return Mono.empty();
        }
    }
}
