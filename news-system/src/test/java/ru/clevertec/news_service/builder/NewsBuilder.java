package ru.clevertec.news_service.builder;

import ru.clevertec.news_service.model.News;

import java.time.LocalDateTime;

public class NewsBuilder implements TestBuilder<News> {

    private Long id = 1L;
    private String title = "Breaking news";
    private String text = "This is a Breaking news!";
    private LocalDateTime time = LocalDateTime.now();
    private String username = "journalist";

    @Override
    public News build() {
        return News.builder()
                .id(this.id)
                .title(this.title)
                .text(this.text)
                .time(this.time)
                .username(this.username)
                .build();
    }
}
