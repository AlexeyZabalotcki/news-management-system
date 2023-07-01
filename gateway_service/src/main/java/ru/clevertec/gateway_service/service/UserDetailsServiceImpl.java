package ru.clevertec.gateway_service.service;

import lombok.RequiredArgsConstructor;
import ru.clevertec.gateway_service.feign_client.UserMsReactiveFeignClient;
import ru.clevertec.gateway_service.security.JwtUser;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserMsReactiveFeignClient userMsReactiveFeignClient;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userMsReactiveFeignClient.findUserByUsername(username)
                .map(JwtUser::new);
    }
}
