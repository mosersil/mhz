package com.silviomoser.demo.api.article;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.data.Article;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.ArticleRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by silvio on 23.07.18.
 */
@RestController
public class ArticleApi {

    @Autowired
    ArticleRepository articleRepository;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List current head article")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Article.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/public/api/article", method = RequestMethod.GET)
    public Article getArticle() {
        return articleRepository.getCurrent(LocalDateTime.now());
    }
}
