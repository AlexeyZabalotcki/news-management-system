package ru.clevertec.news_service.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news_service.dto.AddCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllByKeyword(String keyword);

    CommentPageDto findPageByKeyword(String keyword, Pageable pageable);

    List<CommentDto> findAllByNewsId(Long id);

    CommentPageDto findPageByNewsId(Long id, Pageable pageable);

    List<CommentDto> findAll();

    CommentDto findById(Long id);

    CommentDto add(Long id, AddCommentDto comment);

    CommentDto update(Long id, AddCommentDto updated);

    void deleteById(Long id);

    void deleteAllByNewsId(Long newsId);
}
