package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Article;
import com.silviomoser.demo.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Collection<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article addOrUpdate(Article article) throws ServiceException {
        final Article article1 = articleRepository.save(article);
        log.info("Saved article {}", article1.getId());
        return article1;
    }

    public void delete(Article article) throws ServiceException {
        articleRepository.delete(article);
        log.info("Deleted article {} " + article.getTitle());
    }

    public List<Article> findByTitleLike(String title) throws ServiceException {
        return articleRepository.findByTitleContains(title);
    }

    public int countByTitleLike(String title) throws ServiceException {
        return articleRepository.countByTitleLike(title);
    }
}
