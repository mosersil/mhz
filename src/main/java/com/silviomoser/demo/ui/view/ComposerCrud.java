package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Composer;
import com.silviomoser.demo.services.ComposerService;
import com.silviomoser.demo.services.CompositionService;
import com.silviomoser.demo.services.ServiceException;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.FindAllCrudOperationListener;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringView(name = ComposerCrud.VIEW_NAME)
public class ComposerCrud implements View, HasValue<Set<Composer>> {

    public static final String VIEW_NAME = "composercrud";

    private final static String[] PREVIEW_PROPERTIES = new String[]{"name" };
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"#", "Name"};

    private final static String[] EDITABLE_PROPERTIES = new String[]{"name" };
    private final static String[] EDITABLE_PROPERTIES_CAPTIONS = new String[]{"Name" };


    private Set<Composer> composersSet;

    private GridCrud<Composer> crud = new GridCrud<>(Composer.class);

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private ComposerService composerService;

    private final TextField titleFilter = new TextField();


    @PostConstruct
    public void init() {

        final GridLayoutCrudFormFactory<Composer> formFactory = gridLayoutCrudFormFactory();

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


        titleFilter.setPlaceholder("Suche nach Familiennamen...");
        titleFilter.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(titleFilter);

        Button clearFilters = new Button(null, VaadinIcons.ERASER);
        clearFilters.addClickListener(event -> {
            titleFilter.clear();
        });
        crud.getCrudLayout().addFilterComponent(clearFilters);


        crud.setFindAllOperation(new FindAllCrudOperationListener<Composer>() {
            @Override
            public Collection<Composer> findAll() {
                return composersSet;
            }
        });

        crud.setAddOperation(p -> {
            try {
                return composerService.add(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setUpdateOperation(p -> {
            try {
                return composerService.update(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
        crud.setDeleteOperation(person -> {
            try {
                composerService.delete(person);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
    }

    public Component getViewComponent() {
        return crud;
    }


    private GridLayoutCrudFormFactory gridLayoutCrudFormFactory() {

        final GridLayoutCrudFormFactory<Composer> formFactory = new GridLayoutCrudFormFactory<>(Composer.class, 2, 2);
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

        return formFactory;

    }


    @Override
    public void setValue(Set<Composer> value) {
        this.composersSet=value;
    }

    @Override
    public Set<Composer> getValue() {
        return composersSet;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<Set<Composer>> listener) {
        return null;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {

    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
