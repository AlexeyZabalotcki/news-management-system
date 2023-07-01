package ru.clevertec.user_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.user_service.model.User;
import ru.clevertec.user_service.service.UserService;


@Slf4j
@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayController {

    private final UserService userService;

    @GetMapping("/check/{username}")
    public ResponseEntity<User> checkUserByUsername(@PathVariable("username") String username) {
        log.info("check user {}", username);
        return ResponseEntity.ok(userService.findByUsername(username));
    }
}
