package ru.clevertec.user_service.builder.dto;

import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.dto.InputUserDto;
import ru.clevertec.user_service.model.Role;

public class InputUserDtoBuilderForIntegration implements TestBuilder<InputUserDto> {

    private String username = "vanya";
    private String password = "123456789";
    private Role role = Role.ADMIN;

    @Override
    public InputUserDto build() {
        return InputUserDto.builder()
                .username(this.username)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
