package ru.clevertec.user_service.builder.dto;

import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.dto.RegisterDto;

import java.time.LocalDate;

public class RegisterDtoBuilderForIntegration implements TestBuilder<RegisterDto> {

    private String username = "ivan";
    private String password = "12345678";

    @Override
    public RegisterDto build() {
        return RegisterDto.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
