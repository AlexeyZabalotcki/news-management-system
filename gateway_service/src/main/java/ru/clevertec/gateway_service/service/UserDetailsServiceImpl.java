package ru.clevertec.gateway_service.service;

import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import ru.clevertec.gateway_service.user_ms.User;
import ru.clevertec.gateway_service.security.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final WebClient webClient;
    private final EurekaClient eurekaClient;

    private static final String USER_CHECK_URL = "/gateway/check/{username}";
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private static final String ERROR_LOG = "An error has occurred";


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        String userMsHomePage = eurekaClient.getNextServerFromEureka("user-service", false).getHomePageUrl();

        return webClient.get()
                .uri(userMsHomePage + USER_CHECK_URL, username)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(User.class);
                    } else if (clientResponse.statusCode().is4xxClientError()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(s -> Mono.error(new UsernameNotFoundException(s)));
                    } else {
                        return Mono.error(IllegalStateException::new);
                    }
                })
                .doOnError(error -> log.error(ERROR_LOG, error))
                .map(JwtUser::new);
    }
}
