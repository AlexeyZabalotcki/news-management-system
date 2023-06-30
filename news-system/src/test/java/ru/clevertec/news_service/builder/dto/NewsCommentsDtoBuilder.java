package ru.clevertec.news_service.builder.dto;

import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.dto.AddCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.NewsCommentsDto;
import ru.clevertec.news_service.dto.NewsDto;

import java.util.ArrayList;
import java.util.List;

public class NewsCommentsDtoBuilder implements TestBuilder<NewsCommentsDto> {

    private NewsDto news = new NewsDtoBuilder().build();
    private List<CommentDto> comments = fillList();

    @Override
    public NewsCommentsDto build() {
        return NewsCommentsDto.builder()
                .news(this.news)
                .comments(this.comments)
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
