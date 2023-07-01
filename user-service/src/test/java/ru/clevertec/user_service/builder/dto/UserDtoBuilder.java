package ru.clevertec.user_service.builder.dto;

import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.dto.UserDto;
import ru.clevertec.user_service.model.Role;

public class UserDtoBuilder implements TestBuilder<UserDto> {

    private String username = "admin";
    private Role role = Role.ADMIN;

    @Override
    public UserDto build() {
        return UserDto.builder()
                .username(this.username)
                .role(this.role)
                .build();
    }
}
