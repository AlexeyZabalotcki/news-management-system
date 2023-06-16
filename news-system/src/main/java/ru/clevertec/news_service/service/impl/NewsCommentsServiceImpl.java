package ru.clevertec.news_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news_service.dto.*;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.model.News;
import ru.clevertec.news_service.service.CommentService;
import ru.clevertec.news_service.service.NewsService;
import ru.clevertec.news_service.service.NewsCommentsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsCommentsServiceImpl implements NewsCommentsService {

    private final CommentService commentService;
    private final NewsService newsService;

    private final NewsMapper newsMapper;
    private final CommentMapper commentMapper;

    @Override
    public List<NewsCommentsDto> find(String key) {
        List<News> news = newsService.findAllByKeyword(key);
        List<Comment> commentDtos = commentService.findAllByKeyword(key);

        Set<Long> newsIds = news.stream().map(News::getId).collect(Collectors.toSet());
        Set<Long> newsIdFromComments = commentDtos.stream()
                .map(commentDto -> commentDto.getNews().getId()).collect(Collectors.toSet());

        newsIds.addAll(newsIdFromComments);

        List<NewsCommentsDto> newsCommentsList = new ArrayList<>();
        for (Long newsId : newsIds) {
            NewsDto byId = newsService.findById(newsId);
            List<CommentDto> comments = commentService.findAllByNewsId(newsId).stream()
                    .map(
                            commentDto -> CommentDto.builder()
                                            .username(commentDto.getUsername())
                                            .text(commentDto.getText())
                                            .time(commentDto.getTime())
                                            .newsId(commentDto.getNewsId())
                                            .build())
                    .collect(Collectors.toList());
            NewsCommentsDto newsComments = NewsCommentsDto.builder()
                    .news(byId)
                    .comments(comments)
                    .build();
            newsCommentsList.add(newsComments);
        }
        return newsCommentsList;
    }

    @Override
    public List<NewsDto> findNews(String key) {
        return newsMapper.toDtoList(newsService.findAllByKeyword(key));
    }

    @Override
    public List<CommentDto> findComments(String key) {
        return commentMapper.toDtoList(commentService.findAllByKeyword(key));
    }

    @Override
    public NewsPageDto findNewsPage(String key, Pageable pageable) {
        return newsService.findPageByKeyword(key, pageable);
    }

    @Override
    public CommentPageDto findCommentPage(String key, Pageable pageable) {
        return commentService.findPageByKeyword(key, pageable);
    }
}
