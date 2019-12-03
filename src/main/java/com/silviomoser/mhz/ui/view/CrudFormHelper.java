package com.silviomoser.mhz.ui.view;

import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.services.PersonService;
import com.silviomoser.mhz.utils.FormatUtils;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.renderers.LocalDateRenderer;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import java.time.LocalDate;

public class CrudFormHelper {

    public static void setCaptions(GridLayoutCrudFormFactory<?> formFactory) {
        formFactory.setCancelButtonCaption("Abbrechen");
        formFactory.setButtonCaption(CrudOperation.ADD, "Hinzufügen");
        formFactory.setButtonCaption(CrudOperation.UPDATE, "Änderung speichern");
        formFactory.setButtonCaption(CrudOperation.DELETE, "Löschen");
    }


    public static <T> void setLocalDateRenderer(Grid<T> grid, String property) {
        Grid.Column<T, LocalDate> dateColumn = (Grid.Column<T, LocalDate>) grid.getColumn(property);
        dateColumn.setRenderer(new LocalDateRenderer());
    }


    public static void setPersonComboBox(GridLayoutCrudFormFactory formFactory, String property, PersonService personService) {
        formFactory.setFieldType("person", ComboBox.class);
        formFactory.setFieldCreationListener("person", field -> {
            ComboBox comboBox = (ComboBox) field;
            comboBox.setDataProvider(new ListDataProvider(personService.getAll()));
            comboBox.setItemCaptionGenerator(new ItemCaptionGenerator<Person>() {
                @Override
                public String apply(Person item) {
                    return FormatUtils.toFirstLastNameOrCompany(item);
                }
            });
        });
    }

}
