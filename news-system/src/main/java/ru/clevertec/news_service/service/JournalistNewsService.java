package ru.clevertec.news_service.service;

import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;

public interface JournalistNewsService {

    NewsDto add(AddNewsDto news, String authorization);

    NewsDto update(Long id, AddNewsDto updated, String authorization);

    void deleteById(Long id, String authorization);
}
