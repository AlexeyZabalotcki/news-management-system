package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.AddUsersCommentDto;

public class AddUsersCommentDtoBuilder implements TestBuilder<AddUsersCommentDto> {

    private String text = "this is a text";
    private String username = "subscriber";

    @Override
    public AddUsersCommentDto build() {
        return AddUsersCommentDto.builder()
                .text(this.text)
                .username(this.username)
                .build();
    }
}
