package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Article;
import com.silviomoser.demo.repository.ArticleRepository;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.ui.editor.ArticleEditor;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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
        return new Article("Neuer Anlass", LocalDateTime.now());
    }


}