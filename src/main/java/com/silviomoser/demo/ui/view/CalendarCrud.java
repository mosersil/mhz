package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Article;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.services.ArticleService;
import com.silviomoser.demo.services.CalendarService;
import com.silviomoser.demo.services.ServiceException;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.silviomoser.demo.utils.VaadinUtils.booleanToHtmlValueProvider;

@SpringView(name = CalendarCrud.VIEW_NAME)
public class CalendarCrud implements View {

    public static final String VIEW_NAME = "calendarcrud";

    private final static String[] PREVIEW_PROPERTIES = new String[]{"id", "title", "dateStart","fullDay", "location", "publicEvent"};
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"#", "Titel", "Beginn", "Ganztägig", "Ort", "Öffentlich"};

    private final static String[] EDITABLE_PROPERTIES = new String[]{"title", "dateStart", "dateEnd","location", "dressCode", "fullDay", "publicEvent", "remarks", "article"};
    private final static String[] EDITABLE_PROPERTIES_CAPTIONS = new String[]{"Titel", "Beginn", "Ende", "Ort", "Dresscode", "ganztägig", "öffentlich", "Bemerkungen", "News-Artikel"};


    private I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    private GridCrud<CalendarEvent> crud = new GridCrud<>(CalendarEvent.class);

    private final TextField titleFilter = new TextField();

    private final Button attachmentsButton = new Button(VaadinIcons.CLOUD_DOWNLOAD);

    private final RadioButtonGroup<DressCode> dressCodeRadioButtonGroup = new RadioButtonGroup<>("Dresscode", DataProvider.ofItems(DressCode.values()));

    @Autowired
    CalendarAttachment calendarAttachment;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ArticleService articleService;


    @PostConstruct
    public void init() {

        attachmentsButton.setEnabled(false);

        attachmentsButton.addClickListener(event -> {
            this.getViewComponent().getUI().addWindow(calendarAttachment);
            calendarAttachment.setModal(true);
        });


        dressCodeRadioButtonGroup.setItemCaptionGenerator(it -> i18Helper.getMessage(it.getTag()));



        final GridLayoutCrudFormFactory<CalendarEvent> formFactory = gridLayoutCrudFormFactory();

        crud.setCrudFormFactory(formFactory);


        crud.setUpdateOperationVisible(true);



        crud.setWidth(100, Sizeable.Unit.PERCENTAGE);
        crud.setHeight(600, Sizeable.Unit.PIXELS);

        crud.getGrid().setColumns(PREVIEW_PROPERTIES);
        AtomicInteger i = new AtomicInteger();
        crud.getGrid().getColumns().forEach(it -> {
            it.setCaption(Arrays.asList(PREVIEW_PROPERTIES_CAPTIONS).get(i.get()));
            i.getAndIncrement();
        });


        Grid<CalendarEvent> calendarEventGrid = crud.getGrid();
        Grid.Column<CalendarEvent, LocalDateTime> startColumn = (Grid.Column<CalendarEvent, LocalDateTime>) calendarEventGrid.getColumn("dateStart");
        startColumn.setRenderer(new LocalDateTimeRenderer());


        Grid.Column<CalendarEvent, Boolean> publicEventColumn = (Grid.Column<CalendarEvent, Boolean>) calendarEventGrid.getColumn("publicEvent");
        publicEventColumn.setRenderer(booleanToHtmlValueProvider(), new HtmlRenderer());

        Grid.Column<CalendarEvent, Boolean> fullDayColumn = (Grid.Column<CalendarEvent, Boolean>) calendarEventGrid.getColumn("fullDay");
        fullDayColumn.setRenderer(booleanToHtmlValueProvider(), new HtmlRenderer());


        titleFilter.setPlaceholder("Suche nach Titel...");
        titleFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(titleFilter);

        crud.getCrudLayout().addToolbarComponent(attachmentsButton);

        Button clearFilters = new Button(null, VaadinIcons.ERASER);
        clearFilters.addClickListener(event -> {
            titleFilter.clear();
        });
        crud.getCrudLayout().addFilterComponent(clearFilters);
        crud.getGrid().addItemClickListener(event -> {
            attachmentsButton.setEnabled(true);
           calendarAttachment.setCalendarEvent(event.getItem());
        });
        crud.getGrid().addContextClickListener(event -> {
            attachmentsButton.setEnabled(false);
        });


        crud.setFindAllOperation((FindAllCrudOperationListener<CalendarEvent>) () -> {
            try {
                return calendarService.findByTitleLike(titleFilter.getValue());
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setAddOperation(p -> {
            try {
                return calendarService.addOrUpdate(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setUpdateOperation(p -> {
            try {
                return calendarService.addOrUpdate(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
        crud.setDeleteOperation(article -> {
            try {
                calendarService.delete(article);
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


        formFactory.setFieldType("dateStart", DateTimeField.class);
        formFactory.setFieldType("dateEnd", DateTimeField.class);

        formFactory.setFieldType("article", ComboBox.class);
        formFactory.setFieldCreationListener("article", field -> {
            ComboBox comboBox = (ComboBox) field;
            comboBox.setDataProvider(new ListDataProvider(articleService.findAll()));
            comboBox.setItemCaptionGenerator((ItemCaptionGenerator<Article>) item -> item.getTitle());
        });


        return formFactory;

    }



}
