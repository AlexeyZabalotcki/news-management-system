package ru.clevertec.news_service.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.dto.NewsCommentsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;

import java.util.List;

public interface NewsCommentsService {

    List<NewsCommentsDto> find(String key);

    List<NewsDto> findNews(String key);

    List<CommentDto> findComments(String key);

    NewsPageDto findNewsPage(String key, Pageable pageable);

    CommentPageDto findCommentPage(String key, Pageable pageable);
}
