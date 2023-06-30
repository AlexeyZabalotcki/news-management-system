package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.model.Comment;

import java.time.LocalDateTime;

public class CommentDtoBuilder implements TestBuilder<CommentDto> {

    private Long id = 1L;
    private String text = "This is a comment for news!";
    private LocalDateTime time = LocalDateTime.now();
    private String username = "subscriber";
    private Long newsId = 1L;

    @Override
    public CommentDto build() {
        return CommentDto.builder()
                .text(this.text)
                .time(this.time)
                .username(this.username)
                .newsId(this.newsId)
                .build();
    }
}
