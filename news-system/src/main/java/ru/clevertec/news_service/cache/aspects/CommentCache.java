package ru.clevertec.news_service.cache.aspects;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.clevertec.news_service.cache.Cache;
import ru.clevertec.news_service.cache.CacheFactory;
import ru.clevertec.news_service.dto.CommentDto;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommentCache {

    private final CacheFactory cacheFactory;
    private Cache<String, CommentDto> cache;

    @PostConstruct
    public void init() {
        this.cache = cacheFactory.getCommentCache();
    }


    @Pointcut("execution(* ru.clevertec.news_service.service.impl.CommentServiceImpl.findById(*))")
    public void findById() {
    }

    @Pointcut("execution(* ru.clevertec.news_service.service.impl.CommentServiceImpl.add(..))")
    public void add() {
    }

    @Pointcut("execution(* ru.clevertec.news_service.service.impl.CommentServiceImpl.update(..))")
    public void update() {
    }

    @Pointcut("execution(void ru.clevertec.news_service.service.impl.CommentServiceImpl.deleteById(*))")
    public void deleteById() {
    }

    @AfterReturning(pointcut = "add() || update()", returning = "value")
    public Object addOrUpdateToCache(Object value) {
        CommentDto commentDto = (CommentDto) value;
        if (Objects.nonNull(commentDto)) {
            String key = commentDto.getUsername() + "_" + commentDto.getTime();
            cache.add(key, commentDto);
        }
        return commentDto;
    }

    @Around(value = "findById()")
    public Object findByIdInCache(ProceedingJoinPoint joinPoint) throws Throwable {
        String id = joinPoint.getArgs()[0].toString();
        Optional<CommentDto> optionalCommentDto = cache.get(id);
        if (optionalCommentDto.isEmpty()) {
            CommentDto commentDto = (CommentDto) joinPoint.proceed();
            cache.add(id, commentDto);
            return commentDto;
        }
        return optionalCommentDto.get();
    }

    @After(value = "deleteById()")
    public void deleteBYIdInCache(JoinPoint joinPoint) {
        String id =  joinPoint.getArgs()[0].toString();
        cache.delete(id);
    }
}
