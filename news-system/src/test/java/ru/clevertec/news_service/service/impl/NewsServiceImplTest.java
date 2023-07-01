package ru.clevertec.news_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.AddNewsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsPageDtoBuilder;
import ru.clevertec.news_service.builder.dto.UpdatedNewsDtoBuilder;
import ru.clevertec.news_service.dao.CommentRepository;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.News;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl service;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsMapper newsMapper;

    private static NewsDto newsDto;
    private static News news;
    private static NewsDto updatedNewsDto;
    private static NewsPageDto newsPageDto;
    private static AddNewsDto addNewsDto;
    private static List<News> listNews;
    private static List<NewsDto> dtoListNews;


    @BeforeEach
    void setUp() {
        TestBuilder<NewsDto> newsDtoBuilder = new NewsDtoBuilder();
        newsDto = newsDtoBuilder.build();
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        TestBuilder<NewsPageDto> newsPageDtoBuilder = new NewsPageDtoBuilder();
        newsPageDto = newsPageDtoBuilder.build();
        TestBuilder<AddNewsDto> addNewsDtoBuilder = new AddNewsDtoBuilder();
        addNewsDto = addNewsDtoBuilder.build();
        TestBuilder<NewsDto> updatedNewsDtoBuilder = new UpdatedNewsDtoBuilder();
        updatedNewsDto = updatedNewsDtoBuilder.build();
        listNews = Collections.singletonList(news);
        dtoListNews = Collections.singletonList(newsDto);
    }

    static Stream<Arguments> ids() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(2L),
                Arguments.of(3L),
                Arguments.of(4L)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindAllByKeywordShouldReturnListNews(String keyword) {
        when(newsRepository.findAllByKeword("%" + keyword + "%")).thenReturn(listNews);

        List<News> actual = service.findAllByKeyword(keyword);

        assertEquals(listNews, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindPageByKeywordShouldReturnNewsPage(String keyword) {
        Pageable pageable = PageRequest.of(1, 1);
        Page<News> page = new PageImpl<>(listNews, pageable, listNews.size());
        when(newsRepository.findAllByKeword("%" + keyword + "%", pageable)).thenReturn(page);
        when(newsMapper.toNewsDto(page)).thenReturn(newsPageDto);

        NewsPageDto actual = service.findPageByKeyword(keyword, pageable);

        assertEquals(newsPageDto, actual);
    }

    @Test
    void checkFindAllShouldReturnNewsList() {
        when(newsRepository.findAll()).thenReturn(listNews);
        when(newsMapper.toDto(news)).thenReturn(newsDto);

        List<NewsDto> actual = service.findAll();

        assertEquals(dtoListNews, actual);
    }

    @Test
    void checkFindPageShouldReturnNewsPage() {
        Pageable pageable = PageRequest.of(1, 1);
        Page<News> page = new PageImpl<>(listNews, pageable, listNews.size());
        when(newsRepository.findAll(pageable)).thenReturn(page);
        when(newsMapper.toNewsDto(page)).thenReturn(newsPageDto);

        NewsPageDto actual = service.findPage(pageable);

        assertEquals(newsPageDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkFindByIdShouldReturnExpectedNews(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.ofNullable(news));
        when(newsMapper.toDto(news)).thenReturn(newsDto);

        NewsDto actual = service.findById(id);

        assertEquals(newsDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkFindByIdShouldThrowEntityNotFoundException(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));
    }

    @Test
    void checkAddShouldAddNews() {
        when(newsRepository.save(newsMapper.toEntity(newsDto))).thenReturn(news);
        when(newsMapper.toDto(news)).thenReturn(newsDto);

        NewsDto actual = service.add(addNewsDto);

        assertEquals(newsDto.getTitle(), actual.getTitle());
        assertEquals(newsDto.getText(), actual.getText());
        assertEquals(newsDto.getUsername(), actual.getUsername());
        assertEquals(newsDto.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldUpdateNews(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.ofNullable(news));
        news.setText(updatedNewsDto.getText());
        when(newsRepository.save(news)).thenReturn(news);
        when(newsMapper.toDto(news)).thenReturn(updatedNewsDto);

        NewsDto actual = service.update(id, addNewsDto);

        assertEquals(updatedNewsDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldThrowEntityNotFoundException(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(id, addNewsDto));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldDeleteNews(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.ofNullable(news));
        doNothing().when(commentRepository).deleteAllByNewsId(id);
        doNothing().when(newsRepository).deleteById(id);

        service.deleteById(id);

        verify(commentRepository, times(1)).deleteAllByNewsId(id);
        verify(newsRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldThrowEntityNotFoundException(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.deleteById(id));
    }
}