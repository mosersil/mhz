package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Article;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

@SpringComponent
@UIScope
public class ArticleEditor extends AbstractEditor<Article> {

    final TextField title = new TextField(i18Helper.getMessage("article_title"));
    final TextField teaser = new TextField(i18Helper.getMessage("article_teaser"));
    final RichTextArea text = new RichTextArea("article_text");
    final DateTimeField startDate = new DateTimeField(i18Helper.getMessage("article_from"));
    final DateTimeField endDate = new DateTimeField(i18Helper.getMessage("article_to"));


    @Override
    public Layout initLayout() {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(700);
        text.setSizeFull();
        layout.addComponents(title, text, startDate, endDate);
        return layout;
    }

    @Override
    public Binder initBinder() {
        Binder<Article> binder = new Binder<>(Article.class);

        binder.forField(title)
                .asRequired(i18Helper.getMessage("article_validation_required_title"))
                .withValidator(new StringLengthValidator(i18Helper.getMessage("article_validation_title_length"), 3, 60))
                .bind(Article::getTitle, Article::setTitle);

        binder.forField(text)
                .asRequired("Bitte einen Text eingeben")
                .withValidator(new StringLengthValidator("Bitte Text zwischen 200 und 4000 Zeichen eingeben",200, 4000))
                .bind(Article::getText, Article::setText);

        binder.forField(startDate)
                .asRequired(i18Helper.getMessage("article_validation_required_startdate"));
        binder.forField(endDate)
                .asRequired(i18Helper.getMessage("article_validation_required_enddate"));

        binder.bindInstanceFields(this);

        return binder;
    }


}
