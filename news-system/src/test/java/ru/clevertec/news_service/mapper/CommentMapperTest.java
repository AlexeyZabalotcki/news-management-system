package ru.clevertec.news_service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news_service.builder.CommentBuilder;
import ru.clevertec.news_service.builder.NewsBuilder;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.CommentDtoBuilder;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.model.News;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper mapper;

    @Mock
    private  NewsRepository newsRepository;

    private static CommentDto commentDto;
    private static Comment comment;
    private static News news;
    private static List<Comment> comments;
    private static List<CommentDto> commentsDto;

    @BeforeEach
    void setUp() {
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<Comment> commentTestBuilder = new CommentBuilder();
        comment = commentTestBuilder.build();
        TestBuilder<News> newsBuilder = new NewsBuilder();
        news = newsBuilder.build();
        comments = Collections.singletonList(comment);
        commentsDto = Collections.singletonList(commentDto);
    }

    @Test
    void toDto() {
//        CommentDto actual = mapper.toDto(comment);
//        assertEquals(commentDto, actual);
    }

    @Test
    void toEntity() {
    }

    @Test
    void toDtoList() {
    }

    @Test
    void toEntityList() {
    }

    @Test
    void toPage() {
    }
}