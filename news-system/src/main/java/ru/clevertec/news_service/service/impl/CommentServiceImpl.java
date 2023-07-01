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
import ru.clevertec.news_service.dto.AddCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsService newsService;

    @Override
    public List<Comment> findAllByKeyword(String key) {
        String query = "%" + key + "%";
        return commentRepository.findAllByKeyword(query);
    }

    @Override
    public CommentPageDto findPageByKeyword(String key, Pageable pageable) {
        String query = "%" + key + "%";
        Page<Comment> page = commentRepository.findAllByKeyword(query, pageable);
        return commentMapper.toPage(page);
    }

    @Override
    public CommentPageDto findPageByNewsId(Long id, Pageable pageable) {
        Page<Comment> page = commentRepository.findAllByNewsId(id, pageable);
        return commentMapper.toPage(page);
    }

    @Override
    public List<CommentDto> findAllByNewsId(Long id) {
        List<Comment> commentList = commentRepository.findAllByNewsId(id);
        return commentMapper.toDtoList(commentList);
    }

    @Override
    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Logging
    @Cacheable(value = "comments", key = "#id")
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Check comment id " + id));
        return commentMapper.toDto(comment);
    }

    @Override
    @Logging
    @Transactional
    @CachePut(value = "comments", key = "#result.id")
    public CommentDto add(Long id, AddCommentDto comment) {
        CommentDto toSave = CommentDto.builder()
                .newsId(id)
                .username(comment.getUsername())
                .text(comment.getText())
                .time(LocalDateTime.now())
                .build();
        Comment saved = commentRepository.save(commentMapper.toEntity(toSave));
        return commentMapper.toDto(saved);
    }

    @Override
    @Logging
    @Transactional
    @CachePut(value = "comments", key = "#result.id")
    public CommentDto update(Long id, AddCommentDto updated) {

        Comment forUpdate = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check comment id" + id));

        try {
            Map<String, String> map = commentsToMap(updated);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Field field = Comment.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(forUpdate, value);

            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to update user", ex);
        }
        Comment saved = commentRepository.save(forUpdate);

        return commentMapper.toDto(saved);
    }

    private Map<String, String> commentsToMap(AddCommentDto updates) throws IllegalAccessException {
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

    @Override
    @Logging
    @Transactional
    @CacheEvict(value = "comments", key = "#id")
    public void deleteById(Long id) {
        commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Check if comment with this id exists"));
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByNewsId(Long id) {
        commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Check if comment with this id exists"));
        commentRepository.deleteAllByNewsId(id);
    }
}
