package ru.clevertec.user.service;

import ru.clevertec.user.dto.AuthenticationResponseDto;
import ru.clevertec.user.dto.InputUserDto;
import ru.clevertec.user.dto.LoginDto;
import ru.clevertec.user.dto.RegisterDto;
import ru.clevertec.user.dto.UserDto;
import ru.clevertec.user.model.Role;
import ru.clevertec.user.model.User;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto save(Role role, InputUserDto user);

    UserDto update(Long id, InputUserDto updates);

    User findByUsername(String username);

    void deleteById(Long id);

    AuthenticationResponseDto register(RegisterDto registerRequest);

    AuthenticationResponseDto authenticate(LoginDto registerRequest);
}
