package ru.clevertec.news_service.cache;

import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.NewsDto;

public interface CacheFactory {

    <K, V> Cache<K, V> createCache();

    Cache<String, NewsDto> getNewsCache();

    Cache<String, CommentDto> getCommentCache();
}
