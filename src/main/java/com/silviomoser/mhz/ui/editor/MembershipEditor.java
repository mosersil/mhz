package com.silviomoser.mhz.ui.editor;

import com.silviomoser.mhz.data.Membership;
import com.silviomoser.mhz.data.Organization;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.repository.OrganizationRepository;
import com.silviomoser.mhz.repository.PersonRepository;
import com.silviomoser.mhz.utils.FormatUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class MembershipEditor extends AbstractEditor<Membership> {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    private final ComboBox<Person> person = new ComboBox<>("Person");
    private final ComboBox<Organization> organization = new ComboBox<>("Gruppe");
    private final DateField joinDate = new DateField("Eintritt");
    private final DateField leaveDate = new DateField("Austritt");
    private final TextField function = new TextField("Funktion");
    private final TextArea remarks = new TextArea(i18Helper.getMessage("Bemerkungen"));

    @Override
    public Layout initLayout() {

        person.setDataProvider( new ListDataProvider<Person>(personRepository.findAll()));
        person.setItemCaptionGenerator(it -> FormatUtils.toFirstLastName(it) + " ("+it.getId()+")");
        organization.setDataProvider(new ListDataProvider<Organization>(organizationRepository.findAll()));
        organization.setItemCaptionGenerator(Organization::getName);

        final FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(700);
        layout.addComponents(person, organization, joinDate, leaveDate, function, remarks);

        return layout;
    }

    @Override
    public Binder initBinder() {
        final Binder<Membership> binder = new Binder<>(Membership.class);
        binder.bindInstanceFields(this);
        return binder;
    }
}
