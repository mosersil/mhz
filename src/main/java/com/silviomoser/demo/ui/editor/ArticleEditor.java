package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Article;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.ui.NavigationBar;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static com.silviomoser.demo.utils.VaadinUtils.booleanToHtmlValueProvider;

@SpringComponent
@UIScope
public class ArticleEditor extends AbstractEditor<Article> {

    final TextField title = new TextField(i18Helper.getMessage("article_title"));
    final TextArea text = new TextArea(i18Helper.getMessage("article_text"));
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

        binder.forField(startDate)
                .asRequired(i18Helper.getMessage("article_validation_required_startdate"));
        binder.forField(endDate)
                .asRequired(i18Helper.getMessage("article_validation_required_enddate"));

        binder.bindInstanceFields(this);

        return binder;
    }


}
