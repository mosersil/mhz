package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.repository.StaticFileRepository;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SpringComponent
@UIScope
public class CalendarEditor extends AbstractEditor<CalendarEvent> {

    @Autowired
    private StaticFileRepository staticFileRepository;

    private I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    TextField title = new TextField("Anlass");
    DateTimeField dateStart = new DateTimeField("Datum/Zeit (Beginn)");
    DateTimeField dateEnd = new DateTimeField("Datum/Zeit (Beginn)");
    CheckBox fullDay = new CheckBox("Ganztägiger Anlass");
    CheckBox publicEvent = new CheckBox("Öffentlicher Anlass");
    TextArea remarks = new TextArea("Bemerkungen");
    RadioButtonGroup<DressCode> dressCodeRadioButtonGroup = new RadioButtonGroup<>("Dresscode", DataProvider.ofItems(DressCode.values()));

    private Set<StaticFile> files = new HashSet<>();
    TwinColSelect<StaticFile> filesSelect;


    @Override
    public Layout initLayout() {
        dressCodeRadioButtonGroup.setItemCaptionGenerator(it -> i18Helper.getMessage(it.getTag()));
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(700);
        remarks.setSizeFull();

        title.setWidth(400, Unit.PIXELS);
        fullDay.setDescription("Ein ganztägiger Anlass erscheint ohne genaue von/bis Zeitangabe im Veranstaltungskalender");
        publicEvent.setDescription("Ein öffentlicher Anlass erscheint im Veranstaltungskalender auf der Homepage");


        filesSelect = new TwinColSelect<>("Downloads", staticFileRepository.findAll());
        filesSelect.setRows(6);
        filesSelect.setLeftColumnCaption("Vefügbare Downloads");
        filesSelect.setRightColumnCaption("Diesem Event zugewiesene Downloads");


        // Set full width
        filesSelect.setWidth(100.0f, Unit.PERCENTAGE);

        layout.addComponents(title, dateStart, dateEnd, fullDay, publicEvent, remarks, dressCodeRadioButtonGroup, filesSelect);
        return layout;
    }

    @Override
    public Binder initBinder() {
        Binder<CalendarEvent> binder = new Binder<>(CalendarEvent.class);
        binder.forField(title)
                .asRequired("Bitte Titel vom Anlass angeben")
                .withValidator(new StringLengthValidator("Ungültige Eingabe", 3, 30))
                .bind(CalendarEvent::getTitle, CalendarEvent::setTitle);
        binder.forField(dressCodeRadioButtonGroup)
                .asRequired("Bitte Dresscode wählen")
                .bind(CalendarEvent::getDressCode, CalendarEvent::setDressCode);

        binder.forField(filesSelect).bind(CalendarEvent::getFiles, CalendarEvent::setFiles);
        binder.bindInstanceFields(this);
        return binder;
    }

}
