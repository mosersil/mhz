package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Composer;
import com.silviomoser.demo.data.Composition;
import com.silviomoser.demo.services.ComposerService;
import com.silviomoser.demo.services.CompositionService;
import com.silviomoser.demo.services.ServiceException;
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
import org.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringView(name = CompositionCrud.VIEW_NAME)
public class CompositionCrud implements View {

    private final static String[] PREVIEW_PROPERTIES = new String[]{"tag", "title", "subtitle", "composers", "genre" };
    private final static String[] PREVIEW_PROPERTIES_CAPTIONS = new String[]{"#", "Titel", "Untertitel", "Komponist", "Genre"};

    private final static String[] EDITABLE_PROPERTIES = new String[]{"tag", "title", "subtitle", "composers", "genre" };
    private final static String[] EDITABLE_PROPERTIES_CAPTIONS = new String[]{"Ident.", "Titel", "Untertitel", "Komponist", "Genre" };


    public static final String VIEW_NAME = "compositioncrud";

    private GridCrud<Composition> crud = new GridCrud<>(Composition.class);

    private final TextField titleFilter = new TextField();


    @Autowired
    private CompositionService compositionService;

    @Autowired
    private ComposerService composerService;


    @PostConstruct
    public void init() {

        final GridLayoutCrudFormFactory<Composition> formFactory = gridLayoutCrudFormFactory();

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


        crud.setFindAllOperation(new FindAllCrudOperationListener<Composition>() {
            @Override
            public Collection<Composition> findAll() {
                return compositionService.findByTitleContaining(titleFilter.getValue());
            }
        });

        crud.setAddOperation(p -> {
            try {
                return compositionService.add(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });

        crud.setUpdateOperation(p -> {
            try {
                return compositionService.update(p);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
        crud.setDeleteOperation(person -> {
            try {
                compositionService.delete(person);
            } catch (ServiceException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        });
    }

    public Component getViewComponent() {
        return crud;
    }


    private GridLayoutCrudFormFactory gridLayoutCrudFormFactory() {

        final GridLayoutCrudFormFactory<Composition> formFactory = new GridLayoutCrudFormFactory<>(Composition.class, 2, 2);
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

        formFactory.setFieldType("description", TextArea.class);

        formFactory.setFieldType("composers", ComboBox.class);
        formFactory.setFieldCreationListener("composers", field -> {
            ComboBox comboBox = (ComboBox<Composer>) field;
            comboBox.setDataProvider(new ListDataProvider(composerService.findAll()));
            comboBox.setItemCaptionGenerator((ItemCaptionGenerator<Composer>) item -> item.getName());


            comboBox.setNewItemProvider(s -> {
                Composer composer = new Composer();
                composer.setName(s.toString());
                return Optional.of(composerService.create(composer));
            });
        });

/*
        formFactory.setFieldCreationListener("arrangers", field -> {
            ComboBox comboBox = (ComboBox) field;
            comboBox.setDataProvider(new ListDataProvider(composerService.findAll()));
            comboBox.setItemCaptionGenerator((ItemCaptionGenerator<Composer>) item -> item.getName());


            comboBox.setNewItemProvider(s -> {
                Composer composer = new Composer();
                composer.setName(s);
                return Optional.of(composerService.create(composer));
            });
        });
*/
        return formFactory;

    }

}
