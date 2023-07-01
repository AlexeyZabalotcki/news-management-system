package ru.clevertec.news_service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.NewsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsPageDtoBuilder;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.model.News;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NewsMapperTest {

    @InjectMocks
    private NewsMapper mapper;

    private static News news;
    private static NewsDto newsDto;
    private static NewsPageDto newsPageDto;
    private static List<News> newsList;
    private static List<NewsDto> newsDtoList;

    @BeforeEach
    void setUp() {
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        TestBuilder<NewsDto> newsDtoTestBuilder = new NewsDtoBuilder();
        newsDto = newsDtoTestBuilder.build();
        TestBuilder<NewsPageDto> newsPageDtoBuilder = new NewsPageDtoBuilder();
        newsPageDto = newsPageDtoBuilder.build();
        newsList = Collections.singletonList(news);
        newsDtoList = Collections.singletonList(newsDto);
    }

    @Test
    void checkToDtoShouldReturnDto() {
        NewsDto actual = mapper.toDto(news);

        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
        assertEquals(newsDto.getUsername(), actual.getUsername());
        assertEquals(newsDto.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    void checkToEntityShouldReturnEntity() {
        News actual = mapper.toEntity(newsDto);

        assertEquals(news.getTitle(), actual.getTitle());
        assertEquals(news.getText(), actual.getText());
        assertEquals(news.getUsername(), actual.getUsername());
        assertEquals(news.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    void toDtoList() {
        List<NewsDto> actual = mapper.toDtoList(newsList);

        assertEquals(newsDtoList.size(), actual.size());
    }

    @Test
    void checkToEntityListShoudReturnExpectedListSize() {
        List<News> actual = mapper.toEntityList(newsDtoList);

        assertEquals(newsList.size(), actual.size());
    }

    @Test
    void checkToNewsDtoShouldReturnExpectedPage() {
        Page<News> page = new PageImpl<>(newsList);

        newsPageDto.setNumber(0);
        newsPageDto.setSize(1);
        newsPageDto.setAllElements(1L);
        newsPageDto.setElementsCount(1);

        NewsPageDto actual = mapper.toNewsDto(page);

        assertEquals(newsPageDto.getSize(), actual.getSize());
    }
}
