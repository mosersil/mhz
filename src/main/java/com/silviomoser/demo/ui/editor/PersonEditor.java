package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.type.Gender;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Created by silvio on 29.08.18.
 */
@SpringComponent
@UIScope
public class PersonEditor extends AbstractEditor<Person> {


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
    final DateField birthDate = new DateField("Geburtsdatum");
    final Button initializeAccount = new Button("Account zurücksetzen");
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

        initializeAccount.addClickListener(event -> {


        });

        layout.addComponents(title, genderRadioButtonGroup, firstName, lastName, company, address1, zip, city, landline, mobile, birthDate, remarks, initializeAccount);
        return layout;
    }


    @Override
    public Binder initBinder() {
        Binder<Person> binder = new Binder<>(Person.class);

        binder.forField(firstName).asRequired();
        binder.forField(lastName).asRequired();
        binder.forField(landline).withValidator(new RegexpValidator("Telefonnummer darf nur aus Ziffern bestehen","[0-9]+"));

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
}
