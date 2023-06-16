package ru.clevertec.news_service.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.news_service.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.text like ?1 or c.username like ?1")
    List<Comment> findAllByWordKeys(String keyword);

    @Query("select c from Comment c where c.text like ?1 or c.username like ?1")
    Page<Comment> findAllByWordKeys(String keyword, Pageable pageable);

    List<Comment> findAllByNewsId(Long id);

    Page<Comment> findAllByNewsId(Long id, Pageable pageable);

    void deleteAllByNewsId(Long id);
}
