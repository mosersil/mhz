package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.User;
import com.silviomoser.demo.repository.UserRepository;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.ui.editor.AccountEditor;
import com.silviomoser.demo.utils.FormatUtils;
import com.vaadin.data.Binder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

import static com.silviomoser.demo.utils.FormatUtils.toFirstLastName;

@SpringView(name = AccountView.VIEW_NAME)
public class AccountView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "user";

    TextField userNameEditor = new TextField("Bemerkungen");
    CheckBox enabledField = new CheckBox("Aktiv");

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountEditor accountEditor;

    @PostConstruct
    public void init() {
        this.setDefaultComponentAlignment(Alignment.TOP_LEFT);

        final Collection<User> allUsers = userRepository.findAll();

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        layout.setWidth(100, Unit.PERCENTAGE);
        layout.setHeight(100, Unit.PERCENTAGE);

        addComponent(NavigationBar.buildNavigationBar(this));
        addComponent(layout);


        final Grid<User> grid = new Grid<>();

        grid.setHeight(800, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);

        grid.setItems(allUsers);

        Binder<User> binder = grid.getEditor().getBinder();
        Binder.Binding<User, String> userNameBinding = binder.bind(userNameEditor, User::getUsername, User::setUsername);
        Binder.Binding<User, Boolean> enabledBinding = binder.bind(enabledField, User::isActive, User::setActive);


        grid.addColumn(it -> toFirstLastName(it.getPerson())).setCaption("Person");
        grid.addColumn(it -> it.getUsername()).setCaption("Benutzername").setEditorBinding(userNameBinding).setEditable(true);
        grid.addColumn(it -> it.getCreatedDate()).setCaption("Kreiert am");
        grid.addColumn(it -> it.getLastModifiedDate()).setCaption("Modifiziert am");
        grid.addColumn(it -> it.isActive()).setCaption("Aktiv").setEditorBinding(enabledBinding).setEditable(true);



        grid.getEditor().setEnabled(true);
        grid.getEditor().setBuffered(false);


        final HorizontalLayout groupActions = new HorizontalLayout();
        final TextField filter = new TextField();

        filter.addValueChangeListener(event -> {
            ListDataProvider<User> dataProvider = (ListDataProvider<User>) grid.getDataProvider();
            dataProvider.setFilter(User::getPerson, s -> personNameContains(s, event.getValue()));
        });

        final Button addNewBtn = new Button("Neuer Eintrag", VaadinIcons.PLUS);
        final Button saveChanges = new Button("Änderungen speichern", VaadinIcons.DISC);

        groupActions.addComponents(filter, addNewBtn, saveChanges);

        layout.addComponents(groupActions, grid);


        saveChanges.addClickListener(e -> {
            userRepository.saveAll(allUsers);
            userRepository.flush();
            grid.setItems(userRepository.findAll());
        });

        addNewBtn.addClickListener(item -> {
            //Check, if it is a double-click event
            getUI().addWindow(accountEditor);

            User user = new User();
            accountEditor.editItem(user);


            //add a listener, which will be executed when the window will be closed
            accountEditor.addCloseListener(closeEvent -> {
                getUI().removeWindow(accountEditor);
                grid.setItems(userRepository.findAll());
            });
        });

    }

    private void refresh(List<User> users) {
        users = userRepository.findAll();
    }

    private boolean personNameContains(Person s, String value) {
        return FormatUtils.toFirstLastName(s).contains(value);
    }


}
