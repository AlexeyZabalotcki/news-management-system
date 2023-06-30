package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;

import java.util.ArrayList;
import java.util.List;

public class NewsPageDtoBuilder implements TestBuilder<NewsPageDto> {

    private Integer number = 1;
    private Integer size = 10;
    private Integer allPages = 1;
    private Long allElements = 2L;
    private Integer elementsCount = 1;
    private Boolean first = true;
    private Boolean last = true;
    private List<NewsDto> content = fillList();

    @Override
    public NewsPageDto build() {
        return NewsPageDto.builder()
                .number(this.number)
                .size(this.size)
                .allPages(this.allPages)
                .allElements(this.allElements)
                .elementsCount(this.elementsCount)
                .first(this.first)
                .last(this.last)
                .content(this.content)
                .build();
    }

    private static List<NewsDto> fillList(){
        List<NewsDto> list = new ArrayList<>();
        TestBuilder<NewsDto> builder = new NewsDtoBuilder();
        NewsDto commentDto = builder.build();
        list.add(commentDto);
        return list;
    }
}
