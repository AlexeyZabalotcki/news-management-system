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
import ru.clevertec.news_service.builder.CommentBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.AddCommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.CommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.CommentPageDtoBuilder;
import ru.clevertec.news_service.builder.dto.UpdatedCommentDtoBuilder;
import ru.clevertec.news_service.dao.CommentRepository;
import ru.clevertec.news_service.dto.AddCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.model.Comment;

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
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    private static CommentDto commentDto;
    private static Comment comment;
    private static CommentPageDto commentPageDto;
    private static AddCommentDto addCommentDto;
    private static CommentDto updatedCommentDto;
    private static List<Comment> comments;
    private static List<CommentDto> commentsDto;


    @BeforeEach
    void setUp() {
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<Comment> commentTestBuilder = new CommentBuilder();
        comment = commentTestBuilder.build();
        TestBuilder<CommentPageDto> commentPageDtoTestBuilder = new CommentPageDtoBuilder();
        commentPageDto = commentPageDtoTestBuilder.build();
        comments = Collections.singletonList(comment);
        commentsDto = Collections.singletonList(commentDto);
        TestBuilder<AddCommentDto> addCommentDtoTestBuilder = new AddCommentDtoBuilder();
        addCommentDto = addCommentDtoTestBuilder.build();
        TestBuilder<CommentDto> updateCommentDtoTestBuilder = new UpdatedCommentDtoBuilder();
        updatedCommentDto = updateCommentDtoTestBuilder.build();
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
    void checkFindAllByKeywordShouldReturnListComment(String keyword) {

        when(commentRepository.findAllByKeyword("%" + keyword + "%")).thenReturn(comments);

        List<Comment> actual = commentService.findAllByKeyword(keyword);

        assertEquals(comments, actual);
    }

    @ParameterizedTest
    @ValueSource(strings = "news")
    void checkFindPageByKeywordShouldReturnPage(String keyword) {
        Pageable pageable = PageRequest.of(1, 1);
        Page<Comment> page = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAllByKeyword("%" + keyword + "%", pageable)).thenReturn(page);
        when(commentMapper.toPage(page)).thenReturn(commentPageDto);

        CommentPageDto actual = commentService.findPageByKeyword(keyword, pageable);

        assertEquals(commentPageDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkFindPageByNewsIdShouldReturnPage(Long id) {
        Pageable pageable = PageRequest.of(1, 1);
        Page<Comment> page = new PageImpl<>(comments, pageable, comments.size());
        when(commentRepository.findAllByNewsId(id, pageable)).thenReturn(page);
        when(commentMapper.toPage(page)).thenReturn(commentPageDto);

        CommentPageDto actual = commentService.findPageByNewsId(id, pageable);

        assertEquals(commentPageDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkFindAllByNewsIdShouldReturnNewsList(Long id) {
        when(commentRepository.findAllByNewsId(id)).thenReturn(comments);
        when(commentMapper.toDtoList(comments)).thenReturn(commentsDto);

        List<CommentDto> actual = commentService.findAllByNewsId(id);

        assertEquals(commentsDto, actual);
    }

    @Test
    void checkFindAllShouldReturnCommentsList() {
        when(commentRepository.findAll()).thenReturn(comments);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        List<CommentDto> actual = commentService.findAll();

        assertEquals(commentsDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkFindByIdShouldReturnComment(Long id) {
        when(commentRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto actual = commentService.findById(id);

        assertEquals(commentDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkFindByIdShouldThrowEntityNotFoundException(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentService.findById(id));
    }

    @Test
    void checkAddShouldAddNewComment() {
        when(commentRepository.save(commentMapper.toEntity(commentDto))).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto actual = commentService.add(1L, addCommentDto);

        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getNewsId(), actual.getNewsId());
        assertEquals(commentDto.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldUpdateComment(Long id) {
        when(commentRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(comment));
        comment.setText(updatedCommentDto.getText());
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(updatedCommentDto);

        CommentDto actual = commentService.update(id, addCommentDto);

        assertEquals(updatedCommentDto, actual);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkUpdateShouldThrowEntityNotFoundException(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentService.update(id, addCommentDto));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldDeleteComment(Long id) {
        when(commentRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(comment));
        doNothing().when(commentRepository).deleteById(id);

        commentService.deleteById(id);

        verify(commentRepository, times(1)).deleteById(id);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteByIdShouldThrowEntityNotFoundException(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteById(id));
    }

    @ParameterizedTest
    @MethodSource("ids")
    void deleteAllByNewsId(Long id) {
        when(commentRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(comment));
        doNothing().when(commentRepository).deleteAllByNewsId(id);

        commentService.deleteAllByNewsId(id);

        verify(commentRepository, times(1)).deleteAllByNewsId(id);
    }

    @ParameterizedTest
    @MethodSource("ids")
    void checkDeleteAllByNewsIdShouldThrowEntityNotFoundException(Long id) {
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteAllByNewsId(id));
    }
}