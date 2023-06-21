package ru.clevertec.news_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news_service.dto.AddUsersCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.service.UsersCommentService;


@RestController
@RequestMapping("api/v1/comments")
@RequiredArgsConstructor
public class UsersCommentController {

    private final UsersCommentService commentService;


    @PostMapping("add/{id}")
    public ResponseEntity<CommentDto> add(@RequestHeader String authorization,
                                          @PathVariable Long id,
                                          @Valid @RequestBody AddUsersCommentDto commentDto) {
        return new ResponseEntity<>(commentService.add(id, commentDto,  authorization), HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CommentDto> update(@RequestHeader String authorization,
                                             @PathVariable Long id,
                                             @RequestBody AddUsersCommentDto updatedComment) {
        return new ResponseEntity<>(commentService.update(id, updatedComment, authorization), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CommentDto> deleteById(@RequestHeader String authorization,
                                                 @PathVariable Long id) {
        commentService.deleteById(id, authorization);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
