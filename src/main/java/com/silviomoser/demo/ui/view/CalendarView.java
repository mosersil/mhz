package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.ui.editor.CalendarEditor;
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

/**
 * Created by silvio on 28.07.18.
 */

@SpringView(name = CalendarView.VIEW_NAME)
public class CalendarView extends AbstractCrudView<CalendarEvent> {
    public static final String VIEW_NAME = "calendar";



    @Override
    public void populateGrid(Grid<CalendarEvent> grid) {
        grid.setHeight(400, Unit.PIXELS);
        grid.setWidth(1200, Unit.PIXELS);
        grid.addColumn(CalendarEvent::getId).setCaption("#").setWidth(70);

        grid.addColumn(CalendarEvent::getDate).setRenderer(new LocalDateTimeRenderer(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.GERMAN))).setCaption("Datum/Zeit").setWidth(150);
        grid.addColumn(CalendarEvent::getTitle).setCaption("Anlass");

        grid.addColumn(CalendarEvent::isFullDay).setRenderer(booleanToHtmlValueProvider(), new HtmlRenderer()).setCaption("Ganztägig").setWidth(70);
        grid.addColumn(CalendarEvent::getRemarks).setCaption("Bemerkungen").setMaximumWidth(500);
    }

    @Override
    public CalendarEvent createNew() {
        return new CalendarEvent("Neuer Anlass", LocalDateTime.now());
    }

}

