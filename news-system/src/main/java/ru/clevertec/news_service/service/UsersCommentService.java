package ru.clevertec.news_service.service;

import ru.clevertec.news_service.dto.AddUsersCommentDto;
import ru.clevertec.news_service.dto.CommentDto;

public interface UsersCommentService {

    CommentDto add(Long id, AddUsersCommentDto comment, String authorization);

    CommentDto update(Long id, AddUsersCommentDto updated, String authorization);

    void deleteById(Long id, String authorization);

}
