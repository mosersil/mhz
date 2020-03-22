package com.silviomoser.mhz.ui.view;

import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.type.Gender;
import com.silviomoser.mhz.data.type.PreferredChannel;
import com.silviomoser.mhz.services.CrudServiceException;
import com.silviomoser.mhz.services.PersonService;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.RadioButtonGroupProvider;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringView(name = PersonCrud.VIEW_NAME)
public class PersonCrud  implements View {

    private final static String[] PREVIEW_PROPERTIES = new String[]{"id", "firstName", "lastName", "company", "address1", "zip", "city", "remarks"};
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"#", "Vorname", "Nachname", "Firma", "Adresse", "PLZ", "Ort", "Bemerkungen"};

    private final static String[] EDITABLE_PROPERTIES = new String[]{"gender", "title", "firstName", "lastName", "company", "address1", "address2", "zip", "city", "landline", "mobile", "email", "birthDate", "remarks", "preferredChannel"};
    private final static String[] EDITABLE_PROPERTIES_CAPTIONS = new String[]{"Anrede", "Titel", "Vorname", "Nachname", "Firma", "Adresse", "Adresse (Zusatz)", "PLZ", "Ort", "Telefon", "Mobiltelefon", "Email", "Geburtsdatum", "Bemerkungen", "Komm. Präferenz"};


    public static final String VIEW_NAME = "personcrud";

    private GridCrud<Person> crud = new GridCrud<>(Person.class);

    private final TextField nameFilter = new TextField();


    @Autowired
    PersonService personService;


    @PostConstruct
    public void init() {

        final GridLayoutCrudFormFactory<Person> formFactory = gridLayoutCrudFormFactory();

        crud.setCrudFormFactory(formFactory);

        crud.setUpdateOperationVisible(true);


        crud.setWidth(100, Sizeable.Unit.PERCENTAGE);
        crud.setHeight(800, Sizeable.Unit.PIXELS);

        crud.getGrid().setColumns(PREVIEW_PROPERTIES);
        AtomicInteger i = new AtomicInteger();
        crud.getGrid().getColumns().forEach(it -> {
            it.setCaption(Arrays.asList(PREVIEW_PROPERTIES_CAPTIONS).get(i.get()));
            i.getAndIncrement();
        });


        nameFilter.setPlaceholder("Suche nach Familiennamen...");
        nameFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(nameFilter);

        Button clearFilters = new Button(null, VaadinIcons.ERASER);
        clearFilters.addClickListener(event -> {
            nameFilter.clear();
        });
        crud.getCrudLayout().addFilterComponent(clearFilters);


        crud.setFindAllOperation(new FindAllCrudOperationListener<Person>() {
            @Override
            public Collection<Person> findAll() {
                return personService.findByNameOrCompany(nameFilter.getValue());
            }
        });

        crud.setAddOperation(p -> {
            try {
                return personService.add(p);
            } catch (CrudServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setUpdateOperation(p -> {
            try {
                return personService.update(p);
            } catch (CrudServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
        crud.setDeleteOperation(person -> {
            try {
                personService.delete(person);
            } catch (CrudServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
    }

    public Component getViewComponent() {
     return crud;
    }


    private GridLayoutCrudFormFactory gridLayoutCrudFormFactory() {

        final GridLayoutCrudFormFactory<Person> formFactory = new GridLayoutCrudFormFactory<>(Person.class, 2, 2);
        formFactory.setUseBeanValidation(true);


        formFactory.setFieldProvider("gender", new RadioButtonGroupProvider<>("Anrede", Arrays.asList(Gender.values()), new ItemCaptionGenerator<Gender>() {
            @Override
            public String apply(Gender gender) {
                return ResourceBundle.getBundle("gui_resources").getString(gender.getTag());
            }
        }));


        formFactory.setVisibleProperties(CrudOperation.READ, PREVIEW_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.READ, PREVIEW_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.ADD, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.ADD, EDITABLE_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.UPDATE, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.UPDATE, EDITABLE_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.DELETE, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.DELETE, EDITABLE_PROPERTIES_CAPTIONS);

        CrudFormHelper.setCaptions(formFactory);

        formFactory.setFieldType("remarks", TextArea.class);
        formFactory.setFieldCreationListener("preferredChannel", field -> {
            ComboBox comboBox = (ComboBox) field;
            comboBox.setDataProvider(new ListDataProvider(Arrays.asList(PreferredChannel.values())));
            comboBox.setItemCaptionGenerator((ItemCaptionGenerator<PreferredChannel>) item -> item.getLabel());
        });


        return formFactory;

    }

}
