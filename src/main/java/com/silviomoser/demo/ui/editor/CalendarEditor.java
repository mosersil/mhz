package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.type.DressCode;
import com.silviomoser.demo.repository.RoleRepository;
import com.silviomoser.demo.services.FileHandle;
import com.silviomoser.demo.services.FileService;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

@SpringComponent
@UIScope
public class CalendarEditor extends AbstractEditor<CalendarEvent> {

    @Autowired
    FileService fileService;

    @Autowired
    RoleRepository roleRepository;

    private I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    TextField title = new TextField("Anlass");
    DateTimeField dateStart = new DateTimeField("Datum/Zeit (Beginn)");
    DateTimeField dateEnd = new DateTimeField("Datum/Zeit (Beginn)");
    CheckBox fullDay = new CheckBox("Ganztägiger Anlass");
    CheckBox publicEvent = new CheckBox("Öffentlicher Anlass");
    TextArea remarks = new TextArea("Bemerkungen");
    final FileReceiver fileReceiver = new FileReceiver();
    final Upload upload = new Upload("Anhang hinzufügen", fileReceiver);
    final Grid<StaticFile> fileGrid = new Grid("Datei Anhänge");
    TextField fileNameEditor = new TextField("Name");
    final ComboBox<Role> roleEditor = new ComboBox<>(i18Helper.getMessage("file_authorization"));
    RadioButtonGroup<DressCode> dressCodeRadioButtonGroup = new RadioButtonGroup<>("Dresscode", DataProvider.ofItems(DressCode.values()));

    Button removeSelectedFile = new Button("Remove");

    StaticFile selectedStaticFile;


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


        roleEditor.setItems(roleRepository.findAll());
        roleEditor.setItemCaptionGenerator((ItemCaptionGenerator<Role>) role -> role.getType().getLabel());

        Binder<StaticFile> binder = fileGrid.getEditor().getBinder();
        Binder.Binding<StaticFile, String> titleBinding = binder.bind(fileNameEditor, StaticFile::getTitle, StaticFile::setTitle);
        Binder.Binding<StaticFile, Role> roleBinding = binder.bind(roleEditor, StaticFile::getRole, StaticFile::setRole);

        fileGrid.addColumn(StaticFile::getTitle).setCaption("Titel").setEditorBinding(titleBinding);
        fileGrid.addColumn(StaticFile::getRole).setCaption("Berechtigung").setEditorBinding(roleBinding);
        fileGrid.addColumn(StaticFile::getFileType).setCaption(i18Helper.getMessage("files_type"));



        fileGrid.getEditor().setEnabled(true);
        fileGrid.getEditor().setBuffered(false);
        fileGrid.addItemClickListener(event -> {
            selectedStaticFile = event.getItem();
        });

        removeSelectedFile.addClickListener(event -> {
            actualEntity.getFiles().remove(selectedStaticFile);
            fileGrid.setItems(actualEntity.getFiles());
        });







        // Create the upload with a caption and set receiver later

        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(fileReceiver);

        layout.addComponents(title, dateStart, dateEnd, fullDay, publicEvent, remarks, dressCodeRadioButtonGroup, fileGrid, upload, removeSelectedFile);
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

        //binder.forField(filesSelect).bind(CalendarEvent::getFiles, CalendarEvent::setFiles);
        binder.bindInstanceFields(this);
        return binder;
    }

    public void populateFields() {
        fileGrid.setItems(actualEntity.getFiles());
    }



    // Implement both receiver that saves upload in a file and
    // listener for successful upload
    class FileReceiver implements Upload.Receiver, Upload.SucceededListener {


        FileHandle fileHandle;
        public OutputStream receiveUpload(String filename,
                                          String mimeType) {

            fileHandle = fileService.create(mimeType);

            // Create upload stream
            FileOutputStream fos; // Stream to write to
            try {
                // Open the file for writing.
                fos = new FileOutputStream(fileHandle.getFile());
            } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file<br/>",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            } catch (final IllegalArgumentException e) {
                new Notification(e.getMessage(),
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos; // Return the output stream to write to
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {
            StaticFile staticFile = fileService.save(fileHandle, "Anhang", "", null, null, actualEntity);
            actualEntity.getFiles().add(staticFile);
            fileGrid.setItems(actualEntity.getFiles());
        }
    }
}
