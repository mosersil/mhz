package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.type.FileType;
import com.silviomoser.demo.repository.PersonRepository;
import com.silviomoser.demo.utils.FormatUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@SpringComponent
@UIScope
public class StaticFileEditor extends AbstractEditor<StaticFile> {
    final TextField title = new TextField(i18Helper.getMessage("file_title"));
    final TextArea description = new TextArea(i18Helper.getMessage("file_description"));
    final ComboBox<Person> owner = new ComboBox<>(i18Helper.getMessage("file_owner"));
    final RadioButtonGroup<FileType> fileTypeRadioButtonGroup = new RadioButtonGroup<>(i18Helper.getMessage("file_type"), DataProvider.ofItems(FileType.values()));
    @Autowired
    PersonRepository personRepository;

    @Override
    public Layout initLayout() {


        FileReceiver receiver = new FileReceiver();

        // Create the upload with a caption and set receiver later
        final Upload upload = new Upload("Upload it here", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(700);
        description.setSizeFull();

        owner.setItems(personRepository.findAll());
        owner.setItemCaptionGenerator((ItemCaptionGenerator<Person>) person -> FormatUtils.toFirstLastName(person));

        layout.addComponents(title, description, fileTypeRadioButtonGroup, owner, upload);
        return layout;
    }

    ;

    @Override
    public Binder initBinder() {
        Binder<StaticFile> binder = new Binder<>(StaticFile.class);
        binder.forField(fileTypeRadioButtonGroup)
                .asRequired(i18Helper.getMessage("file_typerequired"))
                .bind(StaticFile::getFileType, StaticFile::setFileType);

        binder.forField(owner)
                .asRequired(i18Helper.getMessage("file_ownerrequired"))
                .bind(StaticFile::getPerson, StaticFile::setPerson);

        binder.bindInstanceFields(this);


        return binder;
    }

    // Implement both receiver that saves upload in a file and
    // listener for successful upload
    class FileReceiver implements Upload.Receiver, Upload.SucceededListener {
        private static final long serialVersionUID = -1276759102490466761L;

        public File file;

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            // Create upload stream
            FileOutputStream fos = null; // Stream to write to
            try {
                // Open the file for writing.
                file = new File("/tmp/" + filename);
                fos = new FileOutputStream(file);
            } catch (final java.io.FileNotFoundException e) {
                new Notification("Could not open file<br/>",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos; // Return the output stream to write to
        }

        public void uploadSucceeded(Upload.SucceededEvent event) {

            // Show the uploaded file in the image viewer
            //image.setVisible(true);
            //image.setSource(new FileResource(file));
            System.out.println("dat funktioniert: " + event.getMIMEType());
            fileTypeRadioButtonGroup.setValue(FileType.byMimeType(event.getMIMEType().toUpperCase()));
        }
    }
}
