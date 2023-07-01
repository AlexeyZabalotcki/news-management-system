package ru.clevertec.news_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.*;
import ru.clevertec.news_service.dao.CommentRepository;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.dto.UserDto;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.News;
import ru.clevertec.news_service.util.CommunicationService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JournalistNewsServiceImplTest {

    @InjectMocks
    private JournalistNewsServiceImpl service;

    @Mock
    private CommunicationService communicationService;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private NewsMapper newsMapper;

    private static News news;
    private static NewsDto newsDto;
    private static NewsDto updatedNewsDto;
    private static AddNewsDto addNewsDto;
    private static UserDto userDto;
    private static String authorization;

    @BeforeEach
    void setUp() {
        TestBuilder<NewsDto> newsDtoBuilder = new NewsDtoBuilder();
        newsDto = newsDtoBuilder.build();
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        TestBuilder<AddNewsDto> addNewsDtoBuilder = new AddNewsDtoBuilder();
        addNewsDto = addNewsDtoBuilder.build();
        TestBuilder<NewsDto> updatedNewsDtoBuilder = new UpdatedNewsDtoBuilder();
        updatedNewsDto = updatedNewsDtoBuilder.build();
        TestBuilder<UserDto> userDtoBuilder = new UserDtoBuilder();
        userDto = userDtoBuilder.build();
        authorization = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiSk9VUk5BTElTVCIsInN1YiI6ImpvdXJuYWxpc3QiLCJpYXQiOjE2ODc0MzEyN" +
                "TgsImV4cCI6MTY4NzQzNDg1OH0.F0X4llX9k-4RiGUiflBcKKvFZzDBsdYvobb3H6L_Elg";
    }

    static Stream<Arguments> ids() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(2L),
                Arguments.of(3L),
                Arguments.of(4L)
        );
    }

    @Test
    void checkAddShouldAddNews() {
        when(communicationService.getUsernameFromToken(authorization)).thenReturn(userDto.getUsername());
        when(newsRepository.save(newsMapper.toEntity(newsDto))).thenReturn(news);
        when(newsMapper.toDto(news)).thenReturn(newsDto);

        NewsDto actual = service.add(addNewsDto, authorization);

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
        when(communicationService.findByUsername(userDto.getUsername())).thenReturn(userDto.getUsername());
        when(communicationService.getUsernameFromToken(authorization)).thenReturn(userDto.getUsername());

        NewsDto actual = service.update(id, addNewsDto, authorization);

        assertEquals(updatedNewsDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldThrowEntityNotFoundException(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(id, addNewsDto, authorization));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldDeleteNews(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.ofNullable(news));
        when(communicationService.findByUsername(userDto.getUsername())).thenReturn(userDto.getUsername());
        when(communicationService.getUsernameFromToken(authorization)).thenReturn(userDto.getUsername());
        doNothing().when(commentRepository).deleteAllByNewsId(id);
        doNothing().when(newsRepository).deleteById(id);

        service.deleteById(id, authorization);

        verify(commentRepository, times(1)).deleteAllByNewsId(id);
        verify(newsRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldThrowEntityNotFoundException(Long id) {
        when(newsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.deleteById(id, authorization));
    }
}