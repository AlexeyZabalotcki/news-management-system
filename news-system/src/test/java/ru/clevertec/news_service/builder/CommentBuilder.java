package ru.clevertec.news_service.builder;

import ru.clevertec.news_service.model.Comment;

import java.time.LocalDateTime;

public class CommentBuilder implements TestBuilder<Comment> {

    private Long id = 1L;
    private String text = "This is a comment for news!";
    private LocalDateTime time = LocalDateTime.now();
    private String username = "subscriber";

    @Override
    public Comment build() {
        return Comment.builder()
                .id(this.id)
                .text(this.text)
                .time(this.time)
                .username(this.username)
                .build();
    }
}
