package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;


@Transactional(readOnly = true)
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE :current BETWEEN a.startDate AND a.endDate")
    public Article getCurrent(@Param("current")LocalDateTime current);
}
