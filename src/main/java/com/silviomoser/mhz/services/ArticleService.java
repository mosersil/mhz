package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Article;
import com.silviomoser.mhz.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleService extends AbstractCrudService<Article> {

    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> findByTitleLike(String title) throws ServiceException {
        return articleRepository.findByTitleContains(title);
    }

    public int countByTitleLike(String title) throws ServiceException {
        return articleRepository.countByTitleLike(title);
    }
}
