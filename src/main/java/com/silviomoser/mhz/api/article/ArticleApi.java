package com.silviomoser.mhz.api.article;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.Article;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.repository.ArticleRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Created by silvio on 23.07.18.
 */
@Slf4j
@RestController
@RequestMapping(value = ArticleApi.API_CONTEXTROOT)
public class ArticleApi extends CrudApi<Article> {

    public static final String API_CONTEXTROOT = "/api/article";
    @Autowired
    ArticleRepository articleRepository;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List current head article")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Article.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping(params = "date")
    public Article getArticles(@RequestParam(name = "date") String date) {
        log.info("date=" + date);
        if (date.equalsIgnoreCase("now")) {
            return articleRepository.getCurrent(LocalDateTime.now());
        } else {
            try {
                return articleRepository.getCurrent(LocalDateTime.parse(date));
            } catch (DateTimeParseException e) {
                throw new ApiException("Invalid date", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
