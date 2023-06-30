package ru.clevertec.news_service.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.news_service.builder.TestBuilder;
import ru.clevertec.news_service.builder.dto.CommentDtoBuilder;
import ru.clevertec.news_service.builder.dto.NewsDtoBuilder;
import ru.clevertec.news_service.cache.Cache;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.NewsDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheFactoryImplTest {

    private CacheFactoryImpl cacheFactory;

    private static NewsDto newsDto;
    private static CommentDto commentDto;

    @BeforeEach
    void setUp() {
        TestBuilder<CommentDto> commentDtoTestBuilder = new CommentDtoBuilder();
        commentDto = commentDtoTestBuilder.build();
        TestBuilder<NewsDto> newsDtoBuilder = new NewsDtoBuilder();
        newsDto = newsDtoBuilder.build();

        cacheFactory = new CacheFactoryImpl();
        ReflectionTestUtils.setField(cacheFactory, "cacheType", "LFU");
        ReflectionTestUtils.setField(cacheFactory, "capacity", 100);
    }

    @Test
    void testCreateCacheReturnsLFUCache() {
        Cache<String, Object> cache = ReflectionTestUtils.invokeMethod(cacheFactory, "createCache");

        assertTrue(cache instanceof LFUCache);
        assertEquals(100, ((LFUCache) cache).getCapacity());
    }

    @Test
    void testCreateCacheReturnsLRUCache() {
        ReflectionTestUtils.setField(cacheFactory, "cacheType", "LRU");

        Cache<String, Object> cache = ReflectionTestUtils.invokeMethod(cacheFactory, "createCache");

        assertTrue(cache instanceof LRUCache);
        assertEquals(100, ((LRUCache) cache).getCapacity());
    }

    @Test
    void testCreateCacheThrowsExceptionForUnknownCacheType() {
        ReflectionTestUtils.setField(cacheFactory, "cacheType", "UnknownCacheType");

        assertThrows(IllegalArgumentException.class, () -> {
            ReflectionTestUtils.invokeMethod(cacheFactory, "createCache");
        });
    }

    @Test
    void testGetNewsCacheReturnsCacheWithNewsDtoValues() {
        Cache<String, NewsDto> cache = cacheFactory.getNewsCache();
        cache.add("news", newsDto);
        assertEquals(Optional.of(newsDto), cache.get("news"));
    }

    @Test
    void testGetCommentCacheReturnsCacheWithCommentDtoValues() {
        Cache<String, CommentDto> cache = cacheFactory.getCommentCache();
        cache.add("comment", commentDto);
        assertEquals(Optional.of(commentDto), cache.get("comment"));
    }
}