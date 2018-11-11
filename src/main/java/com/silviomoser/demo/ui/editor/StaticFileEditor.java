package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.type.FileType;
import com.silviomoser.demo.repository.RoleRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.services.FileHandle;
import com.silviomoser.demo.services.FileService;
import com.vaadin.data.Binder;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.OutputStream;

@SpringComponent
@UIScope
public class StaticFileEditor extends AbstractEditor<StaticFile> {
    final TextField title = new TextField(i18Helper.getMessage("file_title"));
    final TextArea description = new TextArea(i18Helper.getMessage("file_description"));
    final ComboBox<Role> role = new ComboBox<>(i18Helper.getMessage("file_authorization"));

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FileService fileService;

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

        role.setItems(roleRepository.findAll());
        role.setItemCaptionGenerator((ItemCaptionGenerator<Role>) role -> role.getType().getLabel());

        layout.addComponents(title, description, role, upload);
        return layout;
    }


    @Override
    public Binder initBinder() {
        Binder<StaticFile> binder = new Binder<>(StaticFile.class);
        binder.forField(role)
                .bind(StaticFile::getRole, StaticFile::setRole);

        binder.bindInstanceFields(this);
        return binder;
    }

    // Implement both receiver that saves upload in a file and
    // listener for successful upload
    class FileReceiver implements Upload.Receiver, Upload.SucceededListener {
        private static final long serialVersionUID = -1276759102490466761L;

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
            actualEntity.setFileType(FileType.byMimeType(event.getMIMEType().toUpperCase()));
            actualEntity.setLocation(fileHandle.getName());
            actualEntity.setPerson(SecurityUtils.getMe());
        }
    }
}
