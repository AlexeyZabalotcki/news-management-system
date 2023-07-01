package ru.clevertec.news_service.cache.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.news_service.cache.Cache;
import ru.clevertec.news_service.cache.CacheFactory;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.NewsDto;


@Component
public class CacheFactoryImpl implements CacheFactory {
    @Value("${cache.type}")
    private String cacheType;

    @Value("${cache.capacity}")
    private int capacity;

//    @Override
    private <K, V> Cache<K, V> createCache() {
        switch (cacheType) {
            case "LFU":
                return new LFUCache<>(capacity);
            case "LRU":
                return new LRUCache<>(capacity);
            default:
                throw new IllegalArgumentException("Unknown cache type: " + cacheType);
        }
    }

    @Override
    public Cache<String, NewsDto> getNewsCache() {
        return createCache();
    }

    @Override
    public Cache<String, CommentDto> getCommentCache() {
        return createCache();
    }
}
