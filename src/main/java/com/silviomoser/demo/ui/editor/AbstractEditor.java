package com.silviomoser.demo.ui.editor;


import com.silviomoser.demo.data.AbstractEntity;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;

/**
 * Created by silvio on 26.07.18.
 */
public abstract class AbstractEditor<T extends AbstractEntity> extends Window {

    private int width=500;

    I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    Button save = new Button(i18Helper.getMessage("button_save"), VaadinIcons.CHECK);
    Button cancel = new Button(i18Helper.getMessage("button_cancel"));
    Button delete = new Button(i18Helper.getMessage("button_delete"), VaadinIcons.TRASH);

    CssLayout actions = new CssLayout(save, cancel, delete);

    private T actualEntity;


    @Autowired
    private JpaRepository<T, Long> repository;

    private Binder<T> binder;
    private Layout layout;


    public abstract Layout initLayout();

    public abstract Binder initBinder();

    public void setWidth(int width) {
        this.width = width;
    }

    public JpaRepository<T, Long> getRepository() {
        return repository;
    }

    public void setRepository(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public Binder<T> getBinder() {
        return binder;
    }

    public void setBinder(Binder<T> binder) {
        this.binder = binder;
    }

    @PostConstruct
    public void init() {
        layout = initLayout();
        layout.addComponent(actions);
        binder = initBinder();


        setContent(layout);
        setWidth(width, Unit.PIXELS);
        center();

        save.addClickListener(e -> {
            BinderValidationStatus status = binder.validate();
            if (status.isOk()) {
                repository.save(actualEntity);
                close();
            } else {
                save.setComponentError(new UserError(i18Helper.getMessage("error_invalid")));
            }

        });
        delete.addClickListener(e -> {
            ConfirmDialog.show(getUI(), i18Helper.getMessage("confirm_areyousure"),
                    (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            repository.delete(actualEntity);
                            close();
                        } else {
                            close();
                        }
                    });
        });
        cancel.addClickListener(e -> close());
    }


    public final void editItem(T t) {
        if (t == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = t.getId() != null;
        if (persisted) {
            t = repository.findById(t.getId()).get();
        }
        actualEntity = t;
        cancel.setVisible(persisted);
        binder.setBean(t);
        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }
}
