package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.User;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;

@SpringView(name = AccountView.VIEW_NAME)
public class AccountView extends AbstractCrudView<User> {
    public static final String VIEW_NAME = "user";

    @Override
    public void populateGrid(Grid<User> grid) {
        grid.addColumn(User::getId).setCaption("#").setWidth(70);
        grid.addColumn(User::getPerson).setCaption("Person");
        grid.addColumn(User::getCreatedDate).setCaption("Kreiert");
        grid.addColumn(User::getLastModifiedDate).setCaption("Letzte Änderung");
    }

    @Override
    public User createNew() {
        return new User();
    }

    @Override
    public boolean applyFilter(User s, String filterString) {
        return false;
    }
}
