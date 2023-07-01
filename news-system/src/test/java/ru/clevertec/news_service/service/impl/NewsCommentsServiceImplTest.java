package ru.clevertec.news_service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news_service.builder.CommentBuilder;
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.CommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.CommentPageDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsCommentsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsPageDtoBuilder;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.dto.NewsCommentsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.model.News;
import ru.clevertec.news_service.service.CommentService;
import ru.clevertec.news_service.service.NewsService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsCommentsServiceImplTest {

    @InjectMocks
    private NewsCommentsServiceImpl service;

    @Mock
    private CommentService commentService;

    @Mock
    private NewsService newsService;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private CommentMapper commentMapper;

    private static NewsDto newsDto;
    private static News news;
    private static CommentDto commentDto;
    private static Comment comment;
    private static NewsPageDto newsPageDto;
    private static CommentPageDto commentPageDto;
    private static NewsCommentsDto newsCommentsDto;
    private static List<News> listNews;
    private static List<NewsDto> dtoListNews;
    private static List<Comment> comments;
    private static List<CommentDto> commentsDto;
    private static List<NewsCommentsDto> newsCommentsDtoList;

    @BeforeEach
    void setUp() {
        TestBuilder<NewsDto> newsDtoBuilder = new NewsDtoBuilder();
        newsDto = newsDtoBuilder.build();
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        TestBuilder<NewsPageDto> newsPageDtoBuilder = new NewsPageDtoBuilder();
        newsPageDto = newsPageDtoBuilder.build();
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<Comment> commentTestBuilder = new CommentBuilder();
        comment = commentTestBuilder.build();
        TestBuilder<CommentPageDto> commentPageDtoTestBuilder = new CommentPageDtoBuilder();
        commentPageDto = commentPageDtoTestBuilder.build();
        TestBuilder<NewsCommentsDto> newsCommentsDtoTestBuilder = new NewsCommentsDtoBuilder();
        newsCommentsDto = newsCommentsDtoTestBuilder.build();
        listNews = Collections.singletonList(news);
        dtoListNews = Collections.singletonList(newsDto);
        comments = Collections.singletonList(comment);
        commentsDto = Collections.singletonList(commentDto);
        newsCommentsDtoList = Collections.singletonList(newsCommentsDto);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindShouldReturnNewsComments(String keyword) {
        when(newsService.findAllByKeyword(keyword)).thenReturn(listNews);
        when(newsMapper.toDto(news)).thenReturn(newsDto);
        when(commentService.findAllByNewsId(any(Long.class))).thenReturn(commentsDto);

        List<NewsCommentsDto> actual = service.find(keyword);

        assertNotNull( actual);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindNewsShouldReturnNewsList(String keyword) {
        when(newsMapper.toDtoList(newsService.findAllByKeyword(keyword))).thenReturn(dtoListNews);

        List<NewsDto> actual = service.findNews(keyword);

        assertEquals(dtoListNews, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindCommentsShouldReturnCommentsList(String keyword) {
        when(commentMapper.toDtoList(commentService.findAllByKeyword(keyword))).thenReturn(commentsDto);

        List<CommentDto> actual = service.findComments(keyword);

        assertEquals(commentsDto, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindNewsPageShouldReturnNewsPage(String keyword) {
        Pageable pageable = PageRequest.of(1, 1);
        when(newsService.findPageByKeyword(keyword, pageable)).thenReturn(newsPageDto);

        NewsPageDto actual = service.findNewsPage(keyword, pageable);

        assertEquals(newsPageDto, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindCommentPageShouldReturnCommentsPage(String keyword) {
        Pageable pageable = PageRequest.of(1, 1);
        when(commentService.findPageByKeyword(keyword, pageable)).thenReturn(commentPageDto);

        CommentPageDto actual = service.findCommentPage(keyword, pageable);

        assertEquals(commentPageDto, actual);
    }
}