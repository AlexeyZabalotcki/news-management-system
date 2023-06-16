package ru.clevertec.news_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.news_service.dto.AddNewsDto;
import ru.clevertec.news_service.dto.NewsDto;
import ru.clevertec.news_service.dto.NewsPageDto;
import ru.clevertec.news_service.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<NewsDto>> findAll() {
        return new ResponseEntity<>(newsService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(newsService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<NewsPageDto> findPage(@RequestParam(defaultValue = "0", required = false) Integer page,
                                                 @RequestParam(defaultValue = "1", required = false) Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(newsService.findPage(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NewsDto> add(@Valid @RequestBody AddNewsDto news) {
        return new ResponseEntity<>(newsService.add(news), HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<NewsDto> update(@PathVariable Long id, @RequestBody AddNewsDto updatedNews) {
        return new ResponseEntity<>(newsService.update(id, updatedNews), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<NewsDto> deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
