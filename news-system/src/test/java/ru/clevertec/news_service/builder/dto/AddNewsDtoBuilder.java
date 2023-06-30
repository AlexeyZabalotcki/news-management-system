package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.AddNewsDto;

public class AddNewsDtoBuilder implements TestBuilder<AddNewsDto> {

    private String title = "Breaking news";
    private String text = "this is a text";
    private String username = "journalist";

    @Override
    public AddNewsDto build() {
        return AddNewsDto.builder()
                .title(this.title)
                .text(this.text)
                .username(this.username)
                .build();
    }
}
