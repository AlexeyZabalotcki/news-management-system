package ru.clevertec.user.mapper;

import ru.clevertec.user.dto.InputUserDto;
import ru.clevertec.user.dto.UserDto;
import ru.clevertec.user.model.User;
import org.springframework.stereotype.Component;


@Component
public class Mapper {

    public UserDto userToDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public User userToEntity(InputUserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }
}
