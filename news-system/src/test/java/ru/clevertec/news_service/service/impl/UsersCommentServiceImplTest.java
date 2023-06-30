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
import ru.clevertec.news_service.builder.CommentBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.*;
import ru.clevertec.news_service.dao.CommentRepository;
import ru.clevertec.news_service.dto.*;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.util.CommunicationService;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UsersCommentServiceImplTest {

    @InjectMocks
    private UsersCommentServiceImpl service;

    @Mock
    private  CommentRepository commentRepository;

    @Mock
    private  CommunicationService communicationService;

    @Mock
    private  CommentMapper commentMapper;

    private static Comment comment;
    private static CommentDto commentDto;
    private static CommentPageDto commentPageDto;
    private static AddUsersCommentDto addCommentDto;
    private static CommentDto updatedCommentDto;
    private static UserDto userDto;
    private static String authorization;

    @BeforeEach
    void setUp() {
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<Comment> commentTestBuilder = new CommentBuilder();
        comment = commentTestBuilder.build();
        TestBuilder<CommentPageDto> commentPageDtoTestBuilder = new CommentPageDtoBuilder();
        commentPageDto = commentPageDtoTestBuilder.build();
        TestBuilder<AddUsersCommentDto> addCommentDtoTestBuilder = new AddUsersCommentDtoBuilder();
        addCommentDto = addCommentDtoTestBuilder.build();
        TestBuilder<CommentDto> updateCommentDtoTestBuilder = new UpdatedCommentDtoBuilder();
        updatedCommentDto = updateCommentDtoTestBuilder.build();
        TestBuilder<UserDto> userDtoBuilder = new SubscriberDtoBuilder();
        userDto = userDtoBuilder.build();
        authorization = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1VCU0NSSUJFUiIsInN1YiI6InN1YnNjcmliZXIiLCJpYXQiOjE2ODc0MzI" +
                "yNjUsImV4cCI6MTY4NzQzNTg2NX0.r9yMVDw47d6nBY3ZOibkftkQyxPJVVDd55q80AIPRQU";
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
    @MethodSource("ids")
    void checkAddShouldAddComment(Long id) {
        when(communicationService.getUsernameFromToken(authorization)).thenReturn(userDto.getUsername());
        when(commentRepository.save(commentMapper.toEntity(commentDto))).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto actual = service.add(id, addCommentDto, authorization);

        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldUpdateNews(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(comment));
        comment.setText(updatedCommentDto.getText());
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(updatedCommentDto);
        when(communicationService.findByUsername(userDto.getUsername())).thenReturn(userDto.getUsername());
        when(communicationService.getUsernameFromToken(authorization)).thenReturn(userDto.getUsername());

        CommentDto actual = service.update(id, addCommentDto, authorization);

        assertEquals(updatedCommentDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldThrowEntityNotFoundException(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.update(id, addCommentDto, authorization));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldDeleteNews(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(comment));
        when(communicationService.findByUsername(userDto.getUsername())).thenReturn(userDto.getUsername());
        when(communicationService.getUsernameFromToken(authorization)).thenReturn(userDto.getUsername());
        doNothing().when(commentRepository).deleteById(id);

        service.deleteById(id, authorization);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldThrowEntityNotFoundException(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.deleteById(id, authorization));
    }
}