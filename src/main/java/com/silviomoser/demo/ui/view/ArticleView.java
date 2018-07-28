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
public class ArticleView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "article";

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private ArticleEditor window;

    private final Grid<Article> grid = new Grid<>();

    private final Button addNewBtn = new Button("Neuer Head-Artikel", VaadinIcons.PLUS);

    private final TextField filter = new TextField();



    @PostConstruct
    void init() {
        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid);

        addComponent(NavigationBar.buildNavigationBar(this));
        addComponent(mainLayout);


        grid.setHeight(400, Unit.PIXELS);
        grid.setWidth(1200, Unit.PIXELS);
        grid.addColumn(Article::getId).setCaption("#").setWidth(70);
        grid.addColumn(Article::getTitle).setCaption("Titel");
        grid.addColumn(Article::getStartDate).setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.GERMAN))).setCaption("Von").setWidth(150);
        grid.addColumn(Article::getEndDate).setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.GERMAN))).setCaption("Bis").setWidth(150);
        //grid.addColumn(Article::getText).setCaption("Text");


        filter.setPlaceholder("Nach Titel filtern");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listEvents(e.getValue()));

        grid.addItemClickListener(event -> {
            //Check, if it is a double-click event
            if (event.getMouseEventDetails().isDoubleClick()) {
                //get the item which has been clicked
                Article article = event.getItem();
                //open the item in a window
                getUI().addWindow(window);
                window.editItem(article);
                //window.setVisible(true);

                //add a listener, which will be executed when the window will be closed
                window.addCloseListener(closeEvent -> {
                    listEvents(null); //refresh grid to show any changes
                    getUI().removeWindow(window);
                    listEvents(null);
                });
            }
        });

        /*
        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCalendarEvent(e.getValue());
        });
        */

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> {
            window.editItem(new Article("Neuer Head-Artikel", LocalDateTime.now()));
            getUI().addWindow(window);
            //add a listener, which will be executed when the window will be closed
            window.addCloseListener(closeEvent -> {
                listEvents(null); //refresh grid to show any changes
                getUI().removeWindow(window);
                listEvents(null);
            });
        });

        // Listen changes made by the editor, refresh data from backend
        /*
        window.setChangeHandler(() -> {
            window.setVisible(false);
            listEvents(filter.getValue());
        });
        */

        // Initialize listing
        listEvents(null);

    }

    private void listEvents(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repository.findAll());
        }
        else {
            grid.setItems(repository.findAll());
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}