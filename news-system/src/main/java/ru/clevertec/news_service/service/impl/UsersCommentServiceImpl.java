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
import ru.clevertec.news_service.dto.AddUsersCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.mapper.CommentMapper;
import ru.clevertec.news_service.model.Comment;
import ru.clevertec.news_service.service.UsersCommentService;
import ru.clevertec.news_service.util.CommunicationService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersCommentServiceImpl implements UsersCommentService {

    private final CommentRepository commentRepository;
    private final CommunicationService communicationService;
    private final CommentMapper commentMapper;

    @Override
    @Logging
    @Transactional
    @CachePut(value = "comments", key = "#result.id")
    public CommentDto add(Long id, AddUsersCommentDto comment, String authorization) {
        String username = communicationService.getUsernameFromToken(authorization);
        CommentDto toSave = CommentDto.builder()
                .newsId(id)
                .username(username)
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
    public CommentDto update(Long id, AddUsersCommentDto updated, String authorization) {

        String username = updated.getUsername();
        Comment forUpdate = getCommentAndCheckUser(id, username, authorization);

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

    @Override
    @Logging
    @Transactional
    @CacheEvict(value = "comments", key = "#id")
    public void deleteById(Long id, String authorization) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Check comment id: " + id));
        Comment commentAndCheckUser = getCommentAndCheckUser(id, comment.getUsername(), authorization);
        commentRepository.deleteById(id);
    }

    private Map<String, String> commentsToMap(AddUsersCommentDto updates) throws IllegalAccessException {
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

    private Comment getCommentAndCheckUser(Long id, String username, String authorization) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check comment id" + id));

        String usernameFromComment = communicationService.findByUsername(username);

        String usernameFromToken = communicationService.getUsernameFromToken(authorization);

        if (!usernameFromComment.equals(usernameFromToken)){
            throw new RuntimeException("User with username " + usernameFromToken + " can't edit/delete this comment");
        }
        return comment;
    }
}
