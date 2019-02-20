package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.User;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;

import static com.silviomoser.demo.utils.FormatUtils.toFirstLastName;

@SpringView(name = AccountView.VIEW_NAME)
public class AccountView extends AbstractCrudView<User> {
    public static final String VIEW_NAME = "user";


    @Override
    public void populateGrid(Grid<User> grid) {
        grid.addColumn(it -> toFirstLastName(it.getPerson())).setCaption("Person");
        grid.addColumn(it -> it.getUsername()).setCaption("Benutzername");
        grid.addColumn(it -> it.getCreatedDate()).setCaption("Kreiert am");
        grid.addColumn(it -> it.getLastModifiedDate()).setCaption("Modifiziert am");
        grid.addColumn(it -> it.isActive()).setCaption("Aktiv");
    }

    @Override
    public User createNew() {
        return new User();
    }

    @Override
    public boolean applyFilter(User s, String filterString) {
        return s.getUsername().contains(filterString);
    }

}
