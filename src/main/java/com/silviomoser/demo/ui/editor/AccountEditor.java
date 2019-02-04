package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.User;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

@SpringComponent
@UIScope
public class AccountEditor extends AbstractEditor<User> {

    private TextField username = new TextField("Username");
    private DateTimeField createdDate = new DateTimeField("Kreiert am");
    private DateTimeField lastModifiedDate = new DateTimeField("Letzte Änderung am");


    @Override
    public Layout initLayout() {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(1000);

        layout.addComponents(username, createdDate, lastModifiedDate);

        return layout;
    }

    @Override
    public Binder initBinder() {
        Binder<User> binder = new Binder<>(User.class);

        //binder.forField(firstName).asRequired();

        binder.bindInstanceFields(this);
        return binder;

    }
}
