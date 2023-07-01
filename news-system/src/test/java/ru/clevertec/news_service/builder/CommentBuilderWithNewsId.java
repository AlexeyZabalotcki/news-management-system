package ru.clevertec.news_service.builder;

import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.model.News;

import java.time.LocalDateTime;

public class CommentBuilderWithNewsId implements TestBuilder<Comment> {

    private Long id = 1L;
    private String text = "This is a comment for news!";
    private LocalDateTime time = LocalDateTime.now();
    private String username = "subscriber";
    private News news = new NewsBuilder().build();

    @Override
    public Comment build() {
        return Comment.builder()
                .id(this.id)
                .text(this.text)
                .time(this.time)
                .username(this.username)
                .news(news)
                .build();
    }
}
