package ru.clevertec.news_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news_service.dto.*;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.News;
import ru.clevertec.news_service.service.CommentService;
import ru.clevertec.news_service.service.NewsService;
import ru.clevertec.news_service.service.NewsCommentsService;

import java.util.ArrayList;
import java.util.List;

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
    public List<NewsCommentsDto> find(String keyword) {
        List<News> newsList = newsService.findAllByKeyword(keyword);

        List<NewsCommentsDto> newsCommentsList = new ArrayList<>();
        for (News news : newsList) {
            NewsDto newsDto = newsMapper.toDto(news);
            List<CommentDto> commentDtoList = commentService.findAllByNewsId(news.getId());

            NewsCommentsDto newsCommentsDto = NewsCommentsDto.builder()
                    .news(newsDto)
                    .comments(commentDtoList)
                    .build();
            newsCommentsList.add(newsCommentsDto);
        }

        return newsCommentsList;
    }

    @Override
    public List<NewsDto> findNews(String keyword) {
        return newsMapper.toDtoList(newsService.findAllByKeyword(keyword));
    }

    @Override
    public List<CommentDto> findComments(String keyword) {
        return commentMapper.toDtoList(commentService.findAllByKeyword(keyword));
    }

    @Override
    public NewsPageDto findNewsPage(String keyword, Pageable pageable) {
        return newsService.findPageByKeyword(keyword, pageable);
    }

    @Override
    public CommentPageDto findCommentPage(String keyword, Pageable pageable) {
        return commentService.findPageByKeyword(keyword, pageable);
    }
}
