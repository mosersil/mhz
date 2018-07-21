package com.silviomoser.demo.views;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.utils.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

@SpringComponent
@UIScope
public class CalendarDataEditor extends Window {


    private I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    private CalendarEvent calendarEvent;

    @Autowired
    private CalendarEventRepository repository;

    TextField title = new TextField("Anlass");
    DateTimeField date = new DateTimeField("Datum/Zeit");
    CheckBox fullDay = new CheckBox("Ganztägiger Anlass");
    TextArea remarks = new TextArea("Bemerkungen");
    RadioButtonGroup<DressCode> dressCodeRadioButtonGroup = new RadioButtonGroup<>("Dresscode", DataProvider.ofItems(DressCode.values()));



    Button save = new Button("Save", VaadinIcons.CHECK);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcons.TRASH);



    CssLayout actions = new CssLayout(save, cancel, delete);

    Binder<CalendarEvent> binder = new Binder<>(CalendarEvent.class);


    public CalendarDataEditor() {

        dressCodeRadioButtonGroup.setItemCaptionGenerator(it -> i18Helper.getMessage(it.getTag()));

        //Create a form layout which holds all components
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        remarks.setSizeFull();
        layout.addComponents(title, date, fullDay, remarks, dressCodeRadioButtonGroup, actions);

        binder.forField(title)
                .asRequired("test")
                .withValidator(new StringLengthValidator("Invalid", 3, 30))
                .bind(CalendarEvent::getTitle, CalendarEvent::setTitle);
        binder.forField(dressCodeRadioButtonGroup)
                .asRequired("Bitte Dresscode wählen")
                .bind(CalendarEvent::getDressCode, CalendarEvent::setDressCode);

        binder.bindInstanceFields(this);
        binder.setBean(calendarEvent);


        save.addClickListener(e ->  {
            BinderValidationStatus status = binder.validate();
            if (status.isOk()) {
                repository.save(calendarEvent);
                close();
            } else {
                save.setComponentError(new UserError("Ungültig"));
            }

        });
        delete.addClickListener( e -> {

            // The quickest way to confirm
            ConfirmDialog.show(getUI(), "Are you sure?",
                    new ConfirmDialog.Listener() {

                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                // Confirmed to continue
                                repository.delete(calendarEvent);
                                close();
                            } else {
                                // User did not confirm
                                close();
                            }
                        }
                    });


        });
        cancel.addClickListener(e -> close());

        setContent(layout);
        setWidth(500, Unit.PIXELS);

        center();

    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCalendarEvent(CalendarEvent c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            calendarEvent = repository.findById(c.getId()).get();
        }
        else {
            calendarEvent = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(calendarEvent);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
        // Select all text in firstName field automatically
        title.selectAll();
    }

}