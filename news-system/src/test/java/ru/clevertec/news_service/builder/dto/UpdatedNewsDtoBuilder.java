package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.NewsDto;

import java.time.LocalDateTime;

public class UpdatedNewsDtoBuilder implements TestBuilder<NewsDto> {

    private Long id = 1L;
    private String title = "New Breaking news";
    private String text = "This is a Breaking news!";
    private LocalDateTime time = LocalDateTime.now();
    private String username = "journalist";

    @Override
    public NewsDto build() {
        return NewsDto.builder()
                .title(this.title)
                .text(this.text)
                .time(this.time)
                .username(this.username)
                .build();
    }
}
