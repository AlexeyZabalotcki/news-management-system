package ru.clevertec.news_service.cache.aspects;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.clevertec.news_service.cache.Cache;
import ru.clevertec.news_service.cache.CacheFactory;
import ru.clevertec.news_service.dto.NewsDto;

import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class NewsCache {

    private final CacheFactory cacheFactory;
    private Cache<String, NewsDto> cache;

    @PostConstruct
    public void init() {
        this.cache = cacheFactory.getNewsCache();
    }

    @Pointcut("execution(* ru.clevertec.news_service.service.impl.NewsServiceImpl.findById(*))")
    public void findById() {
    }

    @Pointcut("execution(* ru.clevertec.news_service.service.impl.NewsServiceImpl.add(*))")
    public void add() {
    }

    @Pointcut("execution(* ru.clevertec.news_service.service.impl.NewsServiceImpl.update(..))")
    public void update() {
    }

    @Pointcut("execution(void ru.clevertec.news_service.service.impl.NewsServiceImpl.deleteById(*))")
    public void deleteById() {
    }

    @AfterReturning(pointcut = "add() || update()", returning = "value")
    public Object addOrUpdateToCache(Object value) {
        NewsDto newsDto = (NewsDto) value;
        if (Objects.nonNull(newsDto)) {
            String key = newsDto.getTitle() + "_" + newsDto.getTime();
            cache.add(key, newsDto);
        }
        return newsDto;
    }

    @Around(value = "findById()")
    public Object findByIdInCache(ProceedingJoinPoint joinPoint) throws Throwable{
        String id = joinPoint.getArgs()[0].toString();
        Optional<NewsDto> optionalNewsDto = cache.get(id);
        if (optionalNewsDto.isEmpty()) {
            NewsDto newsDto = (NewsDto) joinPoint.proceed();
            cache.add(id, newsDto);
            return newsDto;
        }
        return optionalNewsDto.get();
    }

    @After(value = "deleteById()")
    public void deleteByIdInCache(JoinPoint joinPoint) {
        String id = joinPoint.getArgs()[0].toString();
        cache.delete(id);
    }
}
