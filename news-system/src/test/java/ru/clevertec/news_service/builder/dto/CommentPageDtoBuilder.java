package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;

import java.util.ArrayList;
import java.util.List;

public class CommentPageDtoBuilder implements TestBuilder<CommentPageDto> {

    private Integer number = 1;
    private Integer size = 10;
    private Integer allPages = 1;
    private Long allElements = 2L;
    private Integer elementsCount = 1;
    private Boolean first = true;
    private Boolean last = true;
    private List<CommentDto> content = fillList();

    @Override
    public CommentPageDto build() {
        return CommentPageDto.builder()
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

    private static List<CommentDto> fillList(){
        List<CommentDto> list = new ArrayList<>();
        TestBuilder<CommentDto> builder = new CommentDtoBuilder();
        CommentDto commentDto = builder.build();
        list.add(commentDto);
        return list;
    }
}
