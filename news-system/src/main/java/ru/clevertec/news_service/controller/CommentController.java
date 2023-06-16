package ru.clevertec.news_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news_service.dto.AddCommentDto;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> findAll() {
        return new ResponseEntity<>(commentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> findAllByNewsId(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.findAllByNewsId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/comments/page")
    public ResponseEntity<CommentPageDto>findPageByNewsId(@PathVariable Long id,
                                                          @RequestParam(defaultValue = "0", required = false) Integer page,
                                                          @RequestParam(defaultValue = "1", required = false) Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(commentService.findPageByNewsId(id, pageable), HttpStatus.OK);
    }

    @PostMapping("save/{id}")
    public ResponseEntity<CommentDto> add(@PathVariable Long id,
                                          @Valid @RequestBody AddCommentDto commentDto) {
        return new ResponseEntity<>(commentService.add(id, commentDto), HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, @RequestBody AddCommentDto updatedComment) {
        return new ResponseEntity<>(commentService.update(id, updatedComment), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CommentDto> deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/comments")
    public ResponseEntity<CommentDto> deleteAllByNewsId(@PathVariable Long id) {
        commentService.deleteAllByNewsId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
