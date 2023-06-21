package ru.clevertec.news_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.service.JournalistNewsService;

@RestController
@RequestMapping("api/v1/journalist")
@RequiredArgsConstructor
public class JournalistNewsController {

    private final JournalistNewsService newsService;


    @PostMapping("/add")
    public ResponseEntity<NewsDto> add(@RequestHeader String authorization,
                                       @Valid @RequestBody AddNewsDto news) {
        return new ResponseEntity<>(newsService.add(news, authorization), HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<NewsDto> update(@RequestHeader String authorization,
                                          @PathVariable Long id,
                                          @RequestBody AddNewsDto updatedNews) {
        return new ResponseEntity<>(newsService.update(id, updatedNews, authorization), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<NewsDto> deleteById(@RequestHeader String authorization,
                                              @PathVariable Long id) {
        newsService.deleteById(id, authorization);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
