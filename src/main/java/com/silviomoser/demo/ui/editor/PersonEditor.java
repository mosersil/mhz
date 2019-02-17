package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.type.Gender;
import com.silviomoser.demo.services.PasswordService;
import com.silviomoser.demo.ui.validator.NoEmptyStringValidator;
import com.silviomoser.demo.utils.PersonUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by silvio on 29.08.18.
 */
@SpringComponent
@UIScope
@Slf4j
public class PersonEditor extends AbstractEditor<Person> {

    @Autowired
    PasswordService passwordService;


    final TextField title = new TextField(i18Helper.getMessage("person_title"));
    final TextField firstName = new TextField(i18Helper.getMessage("person_firstname"));
    final TextField lastName = new TextField(i18Helper.getMessage("person_lastname"));
    final TextField company = new TextField(i18Helper.getMessage("person_company"));
    final RadioButtonGroup<Gender> genderRadioButtonGroup = new RadioButtonGroup<>(i18Helper.getMessage("person_gender"), DataProvider.ofItems(Gender.values()));
    final TextField address1 = new TextField(i18Helper.getMessage("person_address"));
    final TextField zip = new TextField(i18Helper.getMessage("person_zip"));
    final TextField city = new TextField(i18Helper.getMessage("person_city"));
    final TextField landline = new TextField(i18Helper.getMessage("person_landline"));
    final TextField mobile = new TextField(i18Helper.getMessage("person_mobile"));
    final TextField email = new TextField("Email");
    final DateField birthDate = new DateField("Geburtsdatum");
    //final Button initializeAccount = new Button("Account erstellen");
    //final Button resetAccount = new Button("Account zurücksetzen");
    private final TextArea remarks = new TextArea(i18Helper.getMessage("person_remarks"));



    @Override
    public Layout initLayout() {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(1000);
        genderRadioButtonGroup.setItemCaptionGenerator(it -> i18Helper.getMessage(it.getTag()));
        firstName.setSizeFull();
        lastName.setSizeFull();
        remarks.setSizeFull();

        /*
        if (actualEntity!=null) {
            if (actualEntity.getUser() == null) {
                resetAccount.setEnabled(false);
            } else {
                resetAccount.setEnabled(true);
            }

            initializeAccount.setEnabled(!resetAccount.isEnabled());
        }

        initializeAccount.addClickListener(event -> {
            try {
                passwordService.createAccount(actualEntity);
            } catch (ServiceException e) {
                log.warn(e.getMessage(), e);
            }
        });

        resetAccount.addClickListener(event -> {
            try {
                passwordService.resetAccount(actualEntity);
            } catch (ServiceException e) {
                log.warn(e.getMessage(), e);
            }
        });
        */

        layout.addComponents(title, genderRadioButtonGroup, firstName, lastName, company, address1, zip, city, landline, mobile, email, birthDate, remarks);
        return layout;
    }


    @Override
    public Binder initBinder() {
        Binder<Person> binder = new Binder<>(Person.class);

        binder.forField(firstName).asRequired("Bitte Vorname eingeben").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).bind(Person::getFirstName, Person::setFirstName);
        binder.forField(lastName).asRequired("Bitte Nachname eingeben").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).bind(Person::getLastName, Person::setLastName);
        binder.forField(company).withNullRepresentation("").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).bind(Person::getCompany, Person::setCompany);
        binder.forField(address1).asRequired("Bitte Adresse eingeben").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).bind(Person::getAddress1, Person::setAddress1);
        binder.forField(zip).asRequired("Bitte PLZ eingeben").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).withValidator(new RegexpValidator("Bitte ausschliesslich Ziffern eingeben", "[0-9]+" )).bind(Person::getZip, Person::setZip);
        binder.forField(city).asRequired("Bitte Ort eingeben").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).bind(Person::getCity, Person::setCity);
        binder.forField(email).withNullRepresentation("").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).withValidator(new EmailValidator("Bitte gültige E-Mail Adresse eingeben")).bind(Person::getEmail, Person::setEmail);
        binder.forField(landline).withNullRepresentation("").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).withValidator(new RegexpValidator("Telefonnummer darf nur aus Ziffern bestehen","[0-9]+")).bind(Person::getLandline, Person::setLandline);
        binder.forField(mobile).withNullRepresentation("").withValidator(new NoEmptyStringValidator("Keine Leerschläge zu Begin/Ende erlaubt")).withValidator(new RegexpValidator("Mobile-Nummer darf nur aus Ziffern bestehen","[0-9]+")).bind(Person::getMobile, Person::setMobile);

        /*
        binder.bind(username, (ValueProvider<Person, String>) person -> person.getUser().getUsername(),
                (Setter<Person, String>) (person, s) -> person.getUser().setUsername(s));

        binder.bind(password, (ValueProvider<Person, String>) person -> person.getUser().getPassword(),
                (Setter<Person, String>) (person, s) -> person.getUser().setPassword(s));
*/

        binder.forField(genderRadioButtonGroup)
                .asRequired(i18Helper.getMessage("person_required_gender"))
                .bind(Person::getGender, Person::setGender);

        binder.bindInstanceFields(this);


        return binder;
    }

    @Override
    public void populateFields() {
        if (isDeleteable(actualEntity)) {
            delete.setEnabled(true);
        }
        else {
            delete.setEnabled(false);
        }
    }

    protected boolean isDeleteable(Person person) {
        return PersonUtils.isDeletable(person);
    }
}
