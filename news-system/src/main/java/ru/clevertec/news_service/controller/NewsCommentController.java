package ru.clevertec.news_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news_service.dto.CommentDto;
import ru.clevertec.news_service.dto.CommentPageDto;
import ru.clevertec.news_service.dto.NewsCommentsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.service.NewsCommentsService;

import java.util.List;

@RestController
@RequestMapping("api/v1/full")
@RequiredArgsConstructor
public class NewsCommentController {

    private final NewsCommentsService service;

    @GetMapping("/find")
    public ResponseEntity<List<NewsCommentsDto>> find(@RequestParam(required = false, defaultValue = "") String keyword) {
        return new ResponseEntity<>(service.find(keyword), HttpStatus.OK);
    }

    @GetMapping("/news")
    public ResponseEntity<List<NewsDto>> findNewsList(@RequestParam(required = false, defaultValue = "") String keyword) {
        return new ResponseEntity<>(service.findNews(keyword), HttpStatus.OK);
    }

    @GetMapping("/news/page")
    public ResponseEntity<NewsPageDto> findNewsPage(@RequestParam(required = false, defaultValue = "") String keyword,
                                                             @RequestParam(defaultValue = "0", required = false) Integer page,
                                                             @RequestParam(defaultValue = "1", required = false) Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(service.findNewsPage(keyword, pageable), HttpStatus.OK);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> findCommentList(@RequestParam(required = false, defaultValue = "") String keyword) {
        return new ResponseEntity<>(service.findComments(keyword), HttpStatus.OK);
    }

    @GetMapping("/comments/page")
    public ResponseEntity<CommentPageDto> findCommentPage(@RequestParam(required = false, defaultValue = "") String keyword,
                                                          @RequestParam(defaultValue = "0", required = false) Integer page,
                                                          @RequestParam(defaultValue = "1", required = false) Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(service.findCommentPage(keyword, pageable), HttpStatus.OK);
    }
}
