package ru.clevertec.news_service.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.model.Comment;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final NewsMapper mapper;
    private final NewsRepository newsRepository;

    public CommentDto toDto(Comment comment){
        return CommentDto.builder()
                .username(comment.getUsername())
                .newsId(comment.getNews().getId())
                .text(comment.getText())
                .time(comment.getTime())
                .build();
    }

    public Comment toEntity(CommentDto dto){
        return Comment.builder()
                .username(dto.getUsername())
                .news(newsRepository.findById(dto.getNewsId())
                        .orElseThrow(() -> new EntityNotFoundException("Check news id " + dto.getNewsId())))
                .text(dto.getText())
                .time(dto.getTime())
                .build();
    }

    public List<CommentDto> toDtoList(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment n : comments) {
            dtos.add(toDto(n));
        }
        return dtos;
    }

    public List<Comment> toEntityList(List<CommentDto> dtos) {
        List<Comment> comments = new ArrayList<>();
        for (CommentDto dto : dtos) {
            comments.add(toEntity(dto));
        }
        return comments;
    }

    public CommentPageDto toPage(Page<Comment> page) {
        return CommentPageDto.builder()
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
