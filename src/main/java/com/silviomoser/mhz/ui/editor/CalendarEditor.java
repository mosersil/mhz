package com.silviomoser.mhz.ui.editor;

import com.silviomoser.mhz.data.CalendarEvent;
import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.data.type.DressCode;
import com.silviomoser.mhz.repository.RoleRepository;
import com.silviomoser.mhz.services.FileHandle;
import com.silviomoser.mhz.services.FileService;
import com.silviomoser.mhz.ui.i18.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

@SpringComponent
@UIScope
public class CalendarEditor extends AbstractEditor<CalendarEvent> {

    @Autowired
    FileService fileService;

    @Autowired
    RoleRepository roleRepository;

    private I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    private final TextField title = new TextField("Anlass");
    private final DateTimeField dateStart = new DateTimeField("Datum/Zeit (Beginn)");
    private final DateTimeField dateEnd = new DateTimeField("Datum/Zeit (Ende)");
    private final CheckBox fullDay = new CheckBox("Ganztägiger Anlass");
    private final CheckBox publicEvent = new CheckBox("Öffentlicher Anlass");
    private final TextArea remarks = new TextArea("Bemerkungen");
    private final FileReceiver fileReceiver = new FileReceiver();
    private final Upload upload = new Upload("Anhang hinzufügen", fileReceiver);
    private final Grid<StaticFile> fileGrid = new Grid("Verknüpfte Anhänge");
    private final TextField fileNameEditor = new TextField("Name");
    private final ComboBox<Role> roleEditor = new ComboBox<>(i18Helper.getMessage("file_authorization"));
    private final RadioButtonGroup<DressCode> dressCodeRadioButtonGroup = new RadioButtonGroup<>("Dresscode", DataProvider.ofItems(DressCode.values()));

    private final Button removeSelectedFile = new Button("Markierte Anhänge entfernen");

    private StaticFile selectedStaticFile;

    private final TabSheet tabSheet = new TabSheet();


    @Override
    public Layout initLayout() {

        tabSheet.setHeight(700.0f, Unit.PIXELS);
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);


        tabSheet.addTab(assembleBaseTab(), "Anlass Daten");
        tabSheet.addTab(assembleAttachmentsTab(), "Downloads");

        final FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(700);

        upload.setButtonCaption("Datei auswählen...");

        layout.addComponents(tabSheet);
        return layout;
    }

    @Override
    public Binder initBinder() {
        final Binder<CalendarEvent> binder = new Binder<>(CalendarEvent.class);
        binder.forField(title)
                .asRequired("Bitte Titel vom Anlass angeben")
                .withValidator(new StringLengthValidator("Ungültige Eingabe", 3, 30))
                .bind(CalendarEvent::getTitle, CalendarEvent::setTitle);
        binder.forField(dressCodeRadioButtonGroup)
                .asRequired("Bitte Dresscode wählen")
                .bind(CalendarEvent::getDressCode, CalendarEvent::setDressCode);

        binder.forField(dateStart).asRequired("Bitte Startdatum/Zeit angeben");
        binder.forField(dateEnd).asRequired("Bitte Enddatum/Zeit angeben oder Anlass als ganztägig markieren");
        binder.bindInstanceFields(this);
        return binder;
    }

    public void populateFields() {
        if (actualEntity.getFiles()!=null) {
            fileGrid.setItems(actualEntity.getFiles());
        } else {
            fileGrid.setItems(new HashSet<>(0));
        }
    }


    private VerticalLayout assembleAttachmentsTab() {

        final VerticalLayout attachmentsTab = new VerticalLayout();

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
            actualEntity.getFiles().remove(selectedStaticFile);
            fileGrid.setItems(actualEntity.getFiles());
        });


        upload.addSucceededListener(fileReceiver);


        attachmentsTab.addComponents(fileGrid, removeSelectedFile, upload);

        return attachmentsTab;
    }


    private VerticalLayout assembleBaseTab() {
        final VerticalLayout baseAttributesTab = new VerticalLayout();

        dressCodeRadioButtonGroup.setItemCaptionGenerator(it -> i18Helper.getMessage(it.getTag()));
        remarks.setSizeFull();

        title.setWidth(400, Unit.PIXELS);
        fullDay.setDescription("Ein ganztägiger Anlass erscheint ohne genaue von/bis Zeitangabe im Veranstaltungskalender");
        publicEvent.setDescription("Ein öffentlicher Anlass erscheint im Veranstaltungskalender auf der Homepage");

        baseAttributesTab.addComponents(title, dateStart, dateEnd, fullDay, publicEvent, remarks, dressCodeRadioButtonGroup);

        return baseAttributesTab;
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
