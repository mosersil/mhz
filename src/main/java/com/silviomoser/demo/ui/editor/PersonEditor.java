package com.silviomoser.demo.ui.editor;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.type.Gender;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by silvio on 29.08.18.
 */
@SpringComponent
@UIScope
public class PersonEditor extends AbstractEditor<Person> {


    final TextField firstName = new TextField(i18Helper.getMessage("person_firstname"));
    final TextField lastName = new TextField(i18Helper.getMessage("person_lastname"));
    final RadioButtonGroup<Gender> genderRadioButtonGroup = new RadioButtonGroup<>(i18Helper.getMessage("person_gender"), DataProvider.ofItems(Gender.values()));
    final TextField address1 = new TextField(i18Helper.getMessage("person_address"));
    final TextField zip = new TextField(i18Helper.getMessage("person_zip"));
    final TextField city = new TextField(i18Helper.getMessage("person_city"));
    final TextField landline = new TextField(i18Helper.getMessage("person_landline"));
    final TextField mobile = new TextField(i18Helper.getMessage("person_mobile"));


    @Override
    public Layout initLayout() {
        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setWidth(1000);
        genderRadioButtonGroup.setItemCaptionGenerator(it -> i18Helper.getMessage(it.getTag()));
        firstName.setSizeFull();
        lastName.setSizeFull();

        layout.addComponents(genderRadioButtonGroup, firstName, lastName, address1, zip, city, landline, mobile);
        return layout;
    }


    @Override
    public Binder initBinder() {
        Binder<Person> binder = new Binder<>(Person.class);

        binder.forField(firstName).asRequired();
        binder.forField(lastName).asRequired();

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
