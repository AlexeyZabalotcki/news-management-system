package ru.clevertec.news_service.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.model.News;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewsMapper {

    public NewsDto toDto(News news) {
        return NewsDto.builder()
                .title(news.getTitle())
                .text(news.getText())
                .time(news.getTime())
                .build();
    }

    public News toEntity(NewsDto dto) {
        return News.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .time(dto.getTime())
                .build();
    }

    public List<NewsDto> toDtoList(List<News> news) {
        List<NewsDto> dtos = new ArrayList<>();
        for (News n : news) {
            dtos.add(toDto(n));
        }
        return dtos;
    }

    public List<News> toEntityList(List<NewsDto> dtos) {
        List<News> news = new ArrayList<>();
        for (NewsDto dto : dtos) {
            news.add(toEntity(dto));
        }
        return news;
    }

    public NewsPageDto toNewsDto(Page<News> page) {
        return NewsPageDto.builder()
                .number(page.getNumber())
                .size(page.getSize())
                .allPages(page.getTotalPages())
                .allElements(page.getTotalElements())
                .first(page.isFirst())
                .elementsCount(page.getNumberOfElements())
                .last(page.isLast())
                .content(toDtoList(page.getContent()))
                .build();
    }
}
