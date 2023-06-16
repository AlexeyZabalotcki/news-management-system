package ru.clevertec.user_service.controller;

import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.dto.UserDto;
import ru.clevertec.user_service.model.Role;
import ru.clevertec.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> allUsers = userService.findAll();
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/save/{role}")
    public ResponseEntity<UserDto> save(@PathVariable Role role,
                                          @Valid @RequestBody InputUserDto user) {
        UserDto saved = userService.save(role, user);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
                                            @Valid @RequestBody InputUserDto updates) {
        UserDto updatedUser = userService.update(id, updates);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<InputUserDto> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
