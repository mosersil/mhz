package com.silviomoser.mhz.ui.view;

import com.silviomoser.mhz.data.Membership;
import com.silviomoser.mhz.data.Organization;
import com.silviomoser.mhz.data.Person;
import com.silviomoser.mhz.services.MembershipService;
import com.silviomoser.mhz.services.OrganizationService;
import com.silviomoser.mhz.services.PersonService;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.ui.i18.I18Helper;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.silviomoser.mhz.ui.view.CrudFormHelper.setLocalDateRenderer;
import static com.silviomoser.mhz.utils.FormatUtils.toFirstLastNameOrCompany;

@SpringView(name = MembershipCrud.VIEW_NAME)
public class MembershipCrud implements View {

    private final static String[] PREVIEW_PROPERTIES = new String[]{"person", "joinDate", "leaveDate", "function", "remarks"};
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"Person/Organisation", "Eintritt", "Austritt", "Funktion", "Bemerkungen"};

    private I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    public static final String VIEW_NAME = "membershiprcrud";




    private final TabSheet tabSheet = new TabSheet();


    @Autowired
    OrganizationService organizationService;

    @Autowired
    MembershipService membershipService;

    @Autowired
    PersonService personService;

    @PostConstruct
    public void init() {

        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        for (Organization organization : organizationService.getAll()) {
            GridCrud<Membership> crud = new GridCrud<>(Membership.class);

            crud.getGrid().setColumns(PREVIEW_PROPERTIES);
            AtomicInteger i = new AtomicInteger();
            crud.getGrid().getColumns().forEach(it -> {
                it.setCaption(Arrays.asList(PREVIEW_PROPERTIES_CAPTIONS).get(i.get()));
                i.getAndIncrement();
            });

            tabSheet.addTab(crud, organization.getName());
            crud.setCrudFormFactory(gridLayoutCrudFormFactory());
            crud.setUpdateOperationVisible(true);


            crud.setDescription("Hier können Personen einer Gruppe zugeteilt werden. Bei einem Rücktritt wird im Normalfall nur ein Austrittsdatum gesetzt. Datensätze bitte nur dann löschen, wenn ein Fehler vorliegt.");
            crud.setHeight(800, Sizeable.Unit.PIXELS);


            Grid.Column<Membership, Person> personColumn = (Grid.Column<Membership, Person>) crud.getGrid().getColumn("person");
            personColumn.setRenderer((ValueProvider<Person, String>) person -> toFirstLastNameOrCompany(person), new TextRenderer());


            setLocalDateRenderer(crud.getGrid(), "joinDate");
            setLocalDateRenderer(crud.getGrid(), "leaveDate");

            final TextField titleFilter = new TextField();
            titleFilter.setPlaceholder("Suche nach Titel...");
            titleFilter.addValueChangeListener(e -> crud.refreshGrid());


            Button clearFilters = new Button(null, VaadinIcons.ERASER);
            clearFilters.addClickListener(event -> {
                titleFilter.clear();
            });
            crud.getCrudLayout().addFilterComponent(clearFilters);
            crud.getCrudLayout().addFilterComponent(titleFilter);



            crud.setFindAllOperation((FindAllCrudOperationListener<Membership>) () -> {
                try {
                    return membershipService.findAllByOrganization(organization, titleFilter.getValue());
                } catch (ServiceException e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            });

            crud.setAddOperation(p -> {
                p.setOrganization(organization);
                try {
                    return membershipService.addOrUpdate(p);
                } catch (ServiceException e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            });

            crud.setUpdateOperation(p -> {
                try {
                    return membershipService.addOrUpdate(p);
                } catch (ServiceException e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            });
            crud.setDeleteOperation(article -> {
                try {
                    membershipService.delete(article);
                } catch (ServiceException e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            });

        }


    }


    public Component getViewComponent() {
        return tabSheet;
    }


    private GridLayoutCrudFormFactory gridLayoutCrudFormFactory() {

        final GridLayoutCrudFormFactory<Membership> formFactory = new GridLayoutCrudFormFactory<>(Membership.class, 1, 6);
        formFactory.setUseBeanValidation(CrudOperation.UPDATE, true);


        formFactory.setVisibleProperties(CrudOperation.ADD, PREVIEW_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.ADD, PREVIEW_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.UPDATE, PREVIEW_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.UPDATE, PREVIEW_PROPERTIES_CAPTIONS);
        formFactory.setVisibleProperties(CrudOperation.DELETE, PREVIEW_PROPERTIES);
        formFactory.setFieldCaptions(CrudOperation.DELETE, PREVIEW_PROPERTIES_CAPTIONS);

        CrudFormHelper.setCaptions(formFactory);


        CrudFormHelper.setPersonComboBox(formFactory, "person", personService);


        formFactory.setDisabledProperties(CrudOperation.UPDATE, "organization", "person");


        return formFactory;

    }

}
