package ru.clevertec.news_service.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.model.News;

import java.util.List;

public interface NewsService {

    List<News> findAllByKeyword(String keyword);

    NewsPageDto findPageByKeyword(String keyword, Pageable pageable);

    List<NewsDto> findAll();

    NewsPageDto findPage(Pageable pageable);

    NewsDto findById(Long id);

    NewsDto add(AddNewsDto news);

    NewsDto update(Long id, AddNewsDto updated);

    void deleteById(Long id);
}
