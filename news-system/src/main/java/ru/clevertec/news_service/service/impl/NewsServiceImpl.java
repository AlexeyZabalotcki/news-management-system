package ru.clevertec.news_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news_service.aop.annotation.Logging;
import ru.clevertec.news_service.dao.CommentRepository;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.News;
import ru.clevertec.news_service.service.NewsService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;

    @Override
    public List<News> findAllByKeyword(String keyword) {
        String word = "%" + keyword + "%";
        return newsRepository.findAllByKeword(word);
    }

    @Override
    public NewsPageDto findPageByKeyword(String keyword, Pageable pageable) {
        String findWord = "%" + keyword + "%";
        Page<News> page = newsRepository.findAllByKeword(findWord, pageable);
        return newsMapper.toNewsDto(page);
    }

    @Override
    public List<NewsDto> findAll() {
        return newsRepository.findAll().stream()
                .map(newsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NewsPageDto findPage(Pageable pageable) {
        Page<News> page = newsRepository.findAll(pageable);
        return newsMapper.toNewsDto(page);
    }

    @Logging
    @Override
    @Cacheable(value = "news", key = "#id")
    public NewsDto findById(Long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Check news id " + id));
        return newsMapper.toDto(news);
    }

    @Logging
    @Override
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public NewsDto add(AddNewsDto news) {
        NewsDto createNews = NewsDto.builder()
                .title(news.getTitle())
                .text(news.getText())
                .username(news.getUsername())
                .time(LocalDateTime.now())
                .build();
        News saved = newsRepository.save(newsMapper.toEntity(createNews));
        return newsMapper.toDto(saved);
    }

    @Logging
    @Override
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public NewsDto update(Long id, AddNewsDto updated) {
        News forUpdate = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Check news id " + id));

        try {
            Map<String, String> map = newsToMap(updated);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Field field = News.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(forUpdate, value);

            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to update user", ex);
        }
        News saved = newsRepository.save(forUpdate);

        return newsMapper.toDto(saved);
    }

    private Map<String, String> newsToMap(AddNewsDto updates) throws IllegalAccessException {
        Map<String, String> values = new HashMap<>();

        Field[] fields = updates.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(updates);
            if (value != null) {
                values.put(field.getName(), value.toString());
            }
        }
        return values;
    }

    @Logging
    @Override
    @Transactional
    @CacheEvict(value = "news", key = "#id")
    public void deleteById(Long id) {
        newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Check if news with this id exists"));
        commentRepository.deleteAllByNewsId(id);
        newsRepository.deleteById(id);

    }
}
