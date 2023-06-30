package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.UserDto;

public class SubscriberDtoBuilder implements TestBuilder<UserDto> {

    private Long id = 1L;
    private String username = "subscriber";

    @Override
    public UserDto build() {
        return UserDto.builder()
                .id(this.id)
                .username(this.username)
                .build();
    }
}
