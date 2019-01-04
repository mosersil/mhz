package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.utils.FormatUtils;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;

/**
 * Created by silvio on 29.08.18.
 */
@SpringView(name = PersonView.VIEW_NAME)
public class PersonView extends AbstractCrudView<Person> {

    public static final String VIEW_NAME = "person";


    @Override
    public void populateGrid(Grid<Person> grid) {
        grid.addColumn(Person::getId).setCaption("#").setWidth(70);
        grid.addColumn(Person::getFirstName).setCaption(i18Helper.getMessage("person_firstname"));
        grid.addColumn(Person::getLastName).setCaption(i18Helper.getMessage("person_lastname"));
        grid.addColumn(Person::getCompany).setCaption(i18Helper.getMessage("person_company"));
        grid.addColumn(Person::getAddress1).setCaption(i18Helper.getMessage("person_address"));
        grid.addColumn(Person::getZip).setCaption(i18Helper.getMessage("person_zip"));
        grid.addColumn(Person::getCity).setCaption(i18Helper.getMessage("person_city"));
        grid.addColumn(Person::getRemarks).setCaption(i18Helper.getMessage("person_remarks"));
    }

    @Override
    public Person createNew() {
        return new Person();
    }

    @Override
    public boolean applyFilter(Person s, String filterString) {
        return FormatUtils.toFirstLastName(s).contains(filterString);
    }

}
