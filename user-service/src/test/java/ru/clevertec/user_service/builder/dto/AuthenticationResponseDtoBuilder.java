package ru.clevertec.user_service.builder.dto;

import ru.clevertec.user_service.builder.TestBuilder;
import ru.clevertec.user_service.dto.AuthenticationResponseDto;

public class AuthenticationResponseDtoBuilder implements TestBuilder<AuthenticationResponseDto> {

    private String accessToken = "12345678910";
    private String refreshToken= "1234567890";

    @Override
    public AuthenticationResponseDto build() {
        return AuthenticationResponseDto.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
    }
}
