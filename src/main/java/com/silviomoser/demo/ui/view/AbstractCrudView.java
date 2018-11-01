package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.AbstractEntity;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.ui.editor.AbstractEditor;
import com.silviomoser.demo.ui.i18.I18Helper;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by silvio on 26.07.18.
 */
public abstract class AbstractCrudView<T extends AbstractEntity> extends VerticalLayout implements View {

    I18Helper i18Helper = new I18Helper(VaadinSession.getCurrent().getLocale());

    private final Grid<T> grid = new Grid<>();

    private Button addNewBtn = new Button("Neuer Eintrag",VaadinIcons.PLUS);

    private final TextField filter = new TextField();


    @Autowired
    private JpaRepository<T, Long> repository;

    @Autowired
    private AbstractEditor<T> abstractEditor;


    public abstract void populateGrid(Grid<T> grid);

    public abstract T createNew();

    @PostConstruct
    void init() {
        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid);

        addComponent(NavigationBar.buildNavigationBar(this));
        addComponent(mainLayout);


        grid.setHeight(400, Unit.PIXELS);
        grid.setWidth(1200, Unit.PIXELS);

        populateGrid(grid);


        filter.setPlaceholder("Nach Titel filtern");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listItems(e.getValue()));

        grid.addItemClickListener(item -> {
            //Check, if it is a double-click event
            if (item.getMouseEventDetails().isDoubleClick()) {
                //get the item which has been clicked
                T article = item.getItem();
                //open the item in a window
                getUI().addWindow(abstractEditor);
                abstractEditor.editItem(article);
                //window.setVisible(true);

                //add a listener, which will be executed when the window will be closed
                abstractEditor.addCloseListener(closeEvent -> {
                    listItems(null); //refresh grid to show any changes
                    getUI().removeWindow(abstractEditor);
                    listItems(null);
                });
            }
        });

        /*
        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCalendarEvent(e.getValue());
        });
        */

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> {
            abstractEditor.editItem(createNew());
            getUI().addWindow(abstractEditor);
            //add a listener, which will be executed when the window will be closed
            abstractEditor.addCloseListener(closeEvent -> {
                listItems(null); //refresh grid to show any changes
                getUI().removeWindow(abstractEditor);
                listItems(null);
            });
        });

        // Listen changes made by the editor, refresh data from backend
        /*
        window.setChangeHandler(() -> {
            window.setVisible(false);
            listItems(filter.getValue());
        });
        */

        // Initialize listing
        listItems(null);

    }

    private void listItems(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repository.findAll());
        }
        else {
            grid.setItems(repository.findAll());
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }

}