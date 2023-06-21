package ru.clevertec.gateway_service.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
        String token = (String) authentication.getCredentials();

        if (jwtTokenProvider.validate(token)) {
            String username = jwtTokenProvider.extractUsername(token);
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
