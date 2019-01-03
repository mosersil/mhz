package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Article;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@SpringView(name = ArticleView.VIEW_NAME)
public class ArticleView extends AbstractCrudView<Article> {
    public static final String VIEW_NAME = "article";


    @Override
    public void populateGrid(Grid<Article> grid) {
        grid.addColumn(Article::getId).setCaption("#").setWidth(70);
        grid.addColumn(Article::getTitle).setCaption("Titel");
        grid.addColumn(Article::getStartDate).setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.GERMAN))).setCaption("Von").setWidth(150);
        grid.addColumn(Article::getEndDate).setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.GERMAN))).setCaption("Bis").setWidth(150);
        //grid.addColumn(Article::getText).setCaption("Text");
    }

    @Override
    public Article createNew() {
        return Article.builder()
                .title(i18Helper.getMessage("article_new_initialtitle"))
                .startDate(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean applyFilter(Article article, String filterString) {
        return article.getTitle().contains(filterString);
    }




}