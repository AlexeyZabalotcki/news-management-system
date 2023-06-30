package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.AddCommentDto;

public class AddCommentDtoBuilder implements TestBuilder<AddCommentDto> {

    private String text = "This is a comment for news!";
    private String username = "subscriber";

    @Override
    public AddCommentDto build() {
        return AddCommentDto.builder()
                .text(this.text)
                .username(this.username)
                .build();
    }
}
