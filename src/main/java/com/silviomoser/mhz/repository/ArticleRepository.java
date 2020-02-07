package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Transactional(readOnly = true)
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @Query("SELECT a FROM Article a WHERE :current BETWEEN a.startDate AND a.endDate")
    List<Article> getCurrent(@Param("current")LocalDateTime current);

    List<Article> findByTitleContains(String title);

    int countByTitleLike(String title);
}
