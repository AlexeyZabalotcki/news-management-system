package ru.clevertec.news_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news_service.aop.annotation.Logging;
import ru.clevertec.news_service.dao.CommentRepository;
import ru.clevertec.news_service.dao.NewsRepository;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.mapper.NewsMapper;
import ru.clevertec.news_service.model.News;
import ru.clevertec.news_service.service.JournalistNewsService;
import ru.clevertec.news_service.util.CommunicationService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JournalistNewsServiceImpl implements JournalistNewsService {

    private final CommunicationService communicationService;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;
    private final NewsMapper newsMapper;

    @Logging
    @Override
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public NewsDto add(AddNewsDto news, String authorization) {
        String journalistUsername = communicationService.getUsernameFromToken(authorization);
        NewsDto createNews = NewsDto.builder()
                .title(news.getTitle())
                .text(news.getText())
                .username(journalistUsername)
                .time(LocalDateTime.now())
                .build();
        News saved = newsRepository.save(newsMapper.toEntity(createNews));
        return newsMapper.toDto(saved);
    }

    @Logging
    @Override
    @Transactional
    @CachePut(value = "news", key = "#result.id")
    public NewsDto update(Long id, AddNewsDto updated, String authorization) {

        String username = updated.getUsername();
        News forUpdate = getNewsAndCheckUser(id, username, authorization);

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
    public void deleteById(Long id, String authorization) {
        News news = newsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Check if news with this id exists"));
        News newsAndCheckUser = getNewsAndCheckUser(id, news.getUsername(), authorization);
        newsRepository.deleteById(id);
        commentRepository.deleteAllByNewsId(id);
    }

    private News getNewsAndCheckUser(Long id, String username, String authorization) {

        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check news id" + id));

        String usernameFromComment = communicationService.findByUsername(username);

        String usernameFromToken = communicationService.getUsernameFromToken(authorization);

        if (!usernameFromComment.equals(usernameFromToken)){
            throw new RuntimeException("User with username " + usernameFromToken + " can't edit/delete this information");
        }
        return news;
    }
}
