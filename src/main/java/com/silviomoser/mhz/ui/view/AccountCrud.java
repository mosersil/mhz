package com.silviomoser.mhz.ui.view;


import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.User;
import com.silviomoser.mhz.repository.RoleRepository;
import com.silviomoser.mhz.services.AccountService;
import com.silviomoser.mhz.services.PersonService;
import com.silviomoser.mhz.services.ServiceException;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.silviomoser.mhz.utils.FormatUtils.toFirstLastName;

@SpringView(name = AccountCrud.VIEW_NAME)
public class AccountCrud implements View {

    public static final String VIEW_NAME = "accountcrud";

    private final static String[] PREVIEW_PROPERTIES = new String[]{"person", "username", "createdDate", "lastModifiedDate", "roles"};
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"Person", "Benutzername", "Kreiert am", "Letzte Änderung", "Rollen"};

    private final static String[] EDITABLE_PROPERTIES = new String[]{"person", "username", "active", "roles"};
    private final static String[] EDITABLE_PROPERTIES_CAPTIONS = new String[]{"Person", "Benutzername", "Aktiv", "Rollen"};

    private GridCrud<User> crud = new GridCrud<>(User.class);

    private final TextField titleFilter = new TextField();

    @Autowired
    private AccountService accountService;

    @Autowired
    private PersonService personService;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {

        final GridLayoutCrudFormFactory<User> formFactory = gridLayoutCrudFormFactory();

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

        Grid.Column<User, Person> personColumn = (Grid.Column<User, Person>) crud.getGrid().getColumn("person");
        personColumn.setRenderer((ValueProvider<Person, String>) person -> toFirstLastName(person), new TextRenderer());

        titleFilter.setPlaceholder("Suche nach Titel...");
        titleFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(titleFilter);

        Button clearFilters = new Button(null, VaadinIcons.ERASER);
        clearFilters.addClickListener(event -> {
            titleFilter.clear();
        });
        crud.getCrudLayout().addFilterComponent(clearFilters);


        crud.setFindAllOperation((FindAllCrudOperationListener<User>) () -> {
            try {
                return accountService.findByUsernameLike(titleFilter.getValue());
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setAddOperation(user -> {
            try {
                return accountService.add(user);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setUpdateOperation(user -> {
            try {
                return accountService.update(user);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
        crud.setDeleteOperation(user -> {
            try {
                accountService.delete(user);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
    }

    public Component getViewComponent() {
        return crud;
    }


    private GridLayoutCrudFormFactory gridLayoutCrudFormFactory() {

        final GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 1, 6);
        formFactory.setUseBeanValidation(true);

        formFactory.setVisibleProperties(CrudOperation.READ, PREVIEW_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.READ, PREVIEW_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.ADD, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.ADD, EDITABLE_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.UPDATE, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.UPDATE, EDITABLE_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.DELETE, EDITABLE_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.DELETE, EDITABLE_PROPERTIES_CAPTIONS);

        CrudFormHelper.setCaptions(formFactory);

        CrudFormHelper.setPersonComboBox(formFactory, "person", personService);

        formFactory.setFieldType("roles", ListSelect.class);
        formFactory.setFieldCreationListener("roles", field -> {
            ListSelect<Role> listSelect = (ListSelect<Role>) field;
            listSelect.setDataProvider(new ListDataProvider(roleRepository.findAll()));
            listSelect.setItemCaptionGenerator(new ItemCaptionGenerator<Role>() {
                @Override
                public String apply(Role item) {
                    return item.getType().name();
                }
            });
        });


        return formFactory;

    }
}
