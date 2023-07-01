package ru.clevertec.user_service.builder.dto;


import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.dto.LoginDto;

public class LoginDtoBuilder implements TestBuilder<LoginDto> {

    private String username = "admin";
    private String password = "12345678";

    @Override
    public LoginDto build() {
        return LoginDto.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
