package ru.clevertec.news_service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.news_service.builder.CommentBuilderWithNewsId;
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.CommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.CommentPageDtoBuilder;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.model.News;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper mapper;

    @Mock
    private NewsRepository newsRepository;

    private static CommentDto commentDto;
    private static Comment comment;
    private static News news;
    private static CommentPageDto commentPageDto;
    private static List<Comment> comments;
    private static List<CommentDto> commentsDto;

    @BeforeEach
    void setUp() {
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<Comment> commentTestBuilder = new CommentBuilderWithNewsId();
        comment = commentTestBuilder.build();
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        TestBuilder<CommentPageDto> commentPageDtoTestBuilder = new CommentPageDtoBuilder();
        commentPageDto = commentPageDtoTestBuilder.build();
        comments = Collections.singletonList(comment);
        commentsDto = Collections.singletonList(commentDto);
    }

    @Test
    void checkToDtoShouldReturnDto() {
        CommentDto actual = mapper.toDto(comment);
        assertEquals(commentDto.getUsername(), actual.getUsername());
        assertEquals(commentDto.getText(), actual.getText());
        assertEquals(commentDto.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertEquals(commentDto.getNewsId(), actual.getNewsId());

    }

    @Test
    void checkToEntityShouldReturnEntity() {
        Long id = 1L;
        when(newsRepository.findById(id)).thenReturn(Optional.ofNullable(news));

        Comment actual = mapper.toEntity(commentDto);
        assertEquals(comment.getUsername(), actual.getUsername());
        assertEquals(comment.getText(), actual.getText());
        assertEquals(comment.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                actual.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    void checkToDtoListShouldReturnDtoList() {
        List<CommentDto> actual = mapper.toDtoList(comments);

        assertEquals(commentsDto.size(), actual.size());
    }

    @Test
    void toEntityList() {
        Long id = 1L;
        when(newsRepository.findById(id)).thenReturn(Optional.ofNullable(news));

        List<Comment> actual = mapper.toEntityList(commentsDto);

        assertEquals(comments.size(), actual.size());
    }

    @Test
    void checkToPageShouldReturnExpectedPageSize() {
        Page<Comment> page = new PageImpl<>(comments);

        commentPageDto.setNumber(0);
        commentPageDto.setSize(1);
        commentPageDto.setAllElements(1L);
        commentPageDto.setElementsCount(1);

        CommentPageDto actual = mapper.toPage(page);

        assertEquals(commentPageDto.getSize(), actual.getSize());
    }
}