package ru.clevertec.news_service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.news_service.model.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n from News n where n.title like ?1 or n.text like ?1")
    List<News> findAllByKeword(String keyword);

    @Query("select n from News n where n.title like ?1 or n.text like ?1")
    Page<News> findAllByKeword(String keyword, Pageable pageable);
}
