package ru.clevertec.user_service.service;

import ru.clevertec.user_service.dto.AuthenticationResponseDto;
import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.dto.LoginDto;
import ru.clevertec.user_service.dto.RegisterDto;
import ru.clevertec.user_service.dto.UserDto;
import ru.clevertec.user_service.model.User;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto save(InputUserDto user);

    UserDto update(Long id, InputUserDto updates);

    User findByUsername(String username);

    void deleteById(Long id);

    AuthenticationResponseDto register(RegisterDto registerRequest);

    AuthenticationResponseDto authenticate(LoginDto registerRequest);
}
