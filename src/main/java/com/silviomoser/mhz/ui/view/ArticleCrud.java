package com.silviomoser.mhz.ui.view;

import com.silviomoser.mhz.data.Article;
import com.silviomoser.mhz.repository.CalendarEventRepository;
import com.silviomoser.mhz.services.ArticleService;
import com.silviomoser.mhz.services.ServiceException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@SpringView(name = ArticleCrud.VIEW_NAME)
public class ArticleCrud implements View {

    private final static String[] PREVIEW_PROPERTIES = new String[]{"id", "title", "teaser", "startDate", "endDate"};
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"#", "Titel", "Vorschau", "Von", "Bis"};

    private final static String[] EDITABLE_PROPERTIES = new String[]{"title", "teaser", "text","startDate", "endDate"};
    private final static String[] EDITABLE_PROPERTIES_CAPTIONS = new String[]{"Titel", "Vorschau", "Text", "Von", "Bis"};


    public static final String VIEW_NAME = "articlecrud";
    private GridCrud<Article> crud = new GridCrud<>(Article.class);

    private final TextField titleFilter = new TextField();

    @Autowired
    ArticleService articleService;

    @Autowired
    CalendarEventRepository calendarEventRepository;

    @PostConstruct
    public void init() {

        final GridLayoutCrudFormFactory<Article> formFactory = gridLayoutCrudFormFactory();

        crud.setCrudFormFactory(formFactory);

        crud.setUpdateOperationVisible(true);


        crud.setWidth(100, Sizeable.Unit.PERCENTAGE);
        crud.setHeight(1200, Sizeable.Unit.PIXELS);

        crud.getGrid().setColumns(PREVIEW_PROPERTIES);
        AtomicInteger i = new AtomicInteger();
        crud.getGrid().getColumns().forEach(it -> {
            it.setCaption(Arrays.asList(PREVIEW_PROPERTIES_CAPTIONS).get(i.get()));
            i.getAndIncrement();
        });


        titleFilter.setPlaceholder("Suche nach Titel...");
        titleFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(titleFilter);

        Button clearFilters = new Button(null, VaadinIcons.ERASER);
        clearFilters.addClickListener(event -> {
            titleFilter.clear();
        });
        crud.getCrudLayout().addFilterComponent(clearFilters);


        crud.setFindAllOperation((FindAllCrudOperationListener<Article>) () -> {
            try {
                return articleService.findByTitleLike(titleFilter.getValue());
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setAddOperation(p -> {
            try {
                return articleService.addOrUpdate(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setUpdateOperation(p -> {
            try {
                return articleService.addOrUpdate(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
        crud.setDeleteOperation(article -> {
            try {
                articleService.delete(article);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
    }

    public Component getViewComponent() {
        return crud;
    }


    private GridLayoutCrudFormFactory gridLayoutCrudFormFactory() {

        final GridLayoutCrudFormFactory<Article> formFactory = new GridLayoutCrudFormFactory<>(Article.class, 1, 6);
        formFactory.setUseBeanValidation(true);


        formFactory.setVisibleProperties(CrudOperation.READ, PREVIEW_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.READ, PREVIEW_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.ADD, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.ADD, EDITABLE_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.UPDATE, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.UPDATE, EDITABLE_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.DELETE, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.DELETE, EDITABLE_PROPERTIES_CAPTIONS);

        CrudFormHelper.setCaptions(formFactory);


        formFactory.setFieldType("text", RichTextArea.class);
        formFactory.setFieldType("startDate", DateTimeField.class);
        formFactory.setFieldType("endDate", DateTimeField.class);
        //formFactory.setFieldType("events", ComboBox.class);


        /*
        formFactory.setFieldProvider("events",
                new TwinColSelectProvider<CalendarEvent>("Anlässe", calendarEventRepository.findAll(), new ItemCaptionGenerator<CalendarEvent>() {
                    @Override
                    public String apply(CalendarEvent item) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        return item.getDateStart().format(formatter) + " " + item.getTitle();
                    }
                }));
*/


        return formFactory;

    }

}
