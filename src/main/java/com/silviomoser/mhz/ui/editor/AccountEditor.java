package com.silviomoser.mhz.ui.editor;

import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.repository.PersonRepository;
import com.silviomoser.mhz.security.utils.PasswordUtils;
import com.silviomoser.mhz.utils.FormatUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class AccountEditor extends AbstractEditor<User> {

    private final TextField username = new TextField("Username");
    private final ComboBox<Person> person = new ComboBox<>("Person");
    private final CheckBox generatePassword = new CheckBox("Generiere Passwort");
    private final CheckBox active = new CheckBox("Aktiv");


    @Autowired
    PersonRepository personRepository;

    @Override
    public Layout initLayout() {
        final FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(1000);

        person.setDataProvider( new ListDataProvider<Person>(personRepository.findWithEmailAddressSet()));
        person.setItemCaptionGenerator(it -> FormatUtils.toFirstLastName(it) + " ("+it.getId()+")");

        person.addValueChangeListener(event -> {
            if (event.getValue()!=null && event.getValue().getEmail()!=null) {
                username.setValue(event.getValue().getEmail());
            }
        });
        layout.addComponents(person, username, active, generatePassword);

        save.addClickListener(e -> {
            if (generatePassword.getValue()) {
                actualEntity.setPassword(PasswordUtils.hashPassword(PasswordUtils.generateToken(12, false)));
            }
        });

        return layout;
    }

    @Override
    public Binder initBinder() {
        Binder<User> binder = new Binder<>(User.class);

        binder.forField(person).asRequired("Bitte Person wählen").bind(User::getPerson, User::setPerson);
        binder.forField(username).asRequired("Bitte Benutzername wählen").bind(User::getUsername, User::setUsername);
        binder.forField(active).bind(User::isActive, User::setActive);

        binder.bindInstanceFields(this);
        return binder;

    }

    public void populateFields() {
        if (actualEntity.getPerson()!=null) {
            person.setEnabled(false);
        }
        else {
            person.setEnabled(true);
        }

    }
}
