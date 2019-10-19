package com.silviomoser.mhz.ui.view;

import com.silviomoser.mhz.data.CalendarEvent;
import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.repository.RoleRepository;
import com.silviomoser.mhz.repository.StaticFileRepository;
import com.silviomoser.mhz.services.FileHandle;
import com.silviomoser.mhz.services.FileService;
import com.silviomoser.mhz.ui.i18.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.OutputStream;

@SpringComponent
@UIScope
public class CalendarAttachment extends Window {


    I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    Button save = new Button(i18Helper.getMessage("button_save"), VaadinIcons.CHECK);
    Button cancel = new Button(i18Helper.getMessage("button_cancel"));
    Button delete = new Button(i18Helper.getMessage("button_delete"), VaadinIcons.TRASH);

    CssLayout actions = new CssLayout(save, cancel, delete);

    private CalendarEvent calendarEvent;

    private StaticFile selectedStaticFile;
    private final CalendarAttachment.FileReceiver fileReceiver = new CalendarAttachment.FileReceiver();
    private final Upload upload = new Upload("Anhang hinzufügen", fileReceiver);

    private final Button removeSelectedFile = new Button("Markierte Anhänge entfernen");

    @Autowired
    private StaticFileRepository staticFileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FileService fileService;

    private final ComboBox<Role> roleEditor = new ComboBox<>(i18Helper.getMessage("file_authorization"));
    private final Grid<StaticFile> fileGrid = new Grid("Verknüpfte Anhänge");
    private final TextField fileNameEditor = new TextField("Name");

    private Binder<StaticFile> binder;
    private Layout layout = new VerticalLayout();



    public void setCalendarEvent(CalendarEvent calendarEvent) {
        this.calendarEvent=calendarEvent;
        if (this.calendarEvent!=null && this.calendarEvent.getFiles()!=null) {
            fileGrid.setItems(this.calendarEvent.getFiles());
        }
    }

    @PostConstruct
    public void init() {


        roleEditor.setItems(roleRepository.findAll());
        roleEditor.setItemCaptionGenerator((ItemCaptionGenerator<Role>) role -> role.getType().getLabel());

        final Binder<StaticFile> binder = fileGrid.getEditor().getBinder();
        final Binder.Binding<StaticFile, String> titleBinding = binder.bind(fileNameEditor, StaticFile::getTitle, StaticFile::setTitle);
        final Binder.Binding<StaticFile, Role> roleBinding = binder.bind(roleEditor, StaticFile::getRole, StaticFile::setRole);

        fileGrid.addColumn(StaticFile::getTitle).setCaption("Titel").setEditorBinding(titleBinding);
        fileGrid.addColumn(StaticFile::getRole).setCaption("Berechtigung").setEditorBinding(roleBinding);
        fileGrid.addColumn(StaticFile::getFileType).setCaption(i18Helper.getMessage("files_type"));



        fileGrid.getEditor().setEnabled(true);
        fileGrid.getEditor().setBuffered(false);
        fileGrid.addItemClickListener(event -> {
            selectedStaticFile = event.getItem();
        });

        //removeSelectedFile.setCaption("Markierte entfernen");

        removeSelectedFile.addClickListener(event -> {
            calendarEvent.getFiles().remove(selectedStaticFile);
            fileGrid.setItems(calendarEvent.getFiles());
        });


        upload.addSucceededListener(fileReceiver);

        save.addClickListener(event -> {
            staticFileRepository.save(selectedStaticFile);
            Notification savedNotification = new Notification("Download wurde gespeichert");
            savedNotification.show(this.getUI().getPage());
        });

        cancel.addClickListener(event -> {
            this.close();
        });


        layout.addComponents(fileGrid, actions, upload);
        //binder = initBinder();


        setContent(layout);
        setWidth(500, Unit.PIXELS);
        setHeightUndefined();
        center();

    }




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
            StaticFile staticFile = fileService.save(fileHandle, "Anhang", "", null, null, calendarEvent);
            calendarEvent.getFiles().add(staticFile);
            fileGrid.setItems(calendarEvent.getFiles());
        }
    }
}
