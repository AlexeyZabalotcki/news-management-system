package ru.clevertec.gateway_service.feign_client;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.clevertec.gateway_service.dto.user.AuthenticationResponseDto;
import ru.clevertec.gateway_service.dto.user.InputUserDto;
import ru.clevertec.gateway_service.dto.user.LoginDto;
import ru.clevertec.gateway_service.dto.user.RegisterDto;
import ru.clevertec.gateway_service.dto.user.User;
import ru.clevertec.gateway_service.dto.user.UserDto;

import java.util.List;

import static ru.clevertec.gateway_service.api.user_ms.UserMsConstants.USER_MS;

@Service
@ReactiveFeignClient(name = USER_MS)
public interface UserMsReactiveFeignClient {

    @GetMapping("/gateway/check/{email}")
    Mono<User> findUserByUsername(@PathVariable String email);

    @PostMapping("/api/v1/auth/register")
    Mono<AuthenticationResponseDto> register(@RequestBody RegisterDto registerDto);

    @PostMapping("/api/v1/auth/auth")
    Mono<AuthenticationResponseDto> login(@RequestBody LoginDto loginDto);

    @GetMapping("/api/v1/auth/refresh")
    Mono<String> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader);

    @GetMapping("/api/v1/users")
    Mono<List<UserDto>> getAll();

    @GetMapping("/api/v1/users/{id}")
    Mono<UserDto> findById(@PathVariable Long id);

    @PostMapping("/api/v1/users/save")
    Mono<UserDto> save(@RequestBody InputUserDto user);

    @PutMapping("/api/v1/users/update/{id}")
    Mono<UserDto> update(@PathVariable Long id,
                         @RequestBody InputUserDto updates);

    @DeleteMapping("/api/v1/users/delete/{id}")
    Mono<UserDto> deleteById(@PathVariable("id") Long id);

}
