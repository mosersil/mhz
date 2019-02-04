package com.silviomoser.demo.ui;

import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.ui.view.ArticleView;
import com.silviomoser.demo.ui.view.CalendarView;
import com.silviomoser.demo.ui.view.MembershipView;
import com.silviomoser.demo.ui.view.PersonView;
import com.silviomoser.demo.ui.view.StaticFileView;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by silvio on 10.05.18.
 */
public class NavigationBar {

    public static Layout buildNavigationBar(AbstractLayout layout) {

        final HorizontalLayout navigationBarItems = new HorizontalLayout();
        navigationBarItems.setId("navigationBarItems");
        navigationBarItems.setHeight(50, Sizeable.Unit.PIXELS);
        //navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBarItems.addComponent(createNavigationButton(layout, "Home", ""));
        navigationBarItems.addComponent(createNavigationButton(layout, "Head-Artikel", ArticleView.VIEW_NAME));
        navigationBarItems.addComponent(createNavigationButton(layout, "Kalender", CalendarView.VIEW_NAME));
        navigationBarItems.addComponent(createNavigationButton(layout, "Personen", PersonView.VIEW_NAME));
        navigationBarItems.addComponent(createNavigationButton(layout, "Gruppen", MembershipView.VIEW_NAME));
        //navigationBarItems.addComponent(createNavigationButton(layout, "Accounts", AccountView.VIEW_NAME));
        navigationBarItems.addComponent(createNavigationButton(layout, "Downloads", StaticFileView.VIEW_NAME));

        final HorizontalLayout navigationBar = new HorizontalLayout();
        navigationBar.setId("navigationBar");
        navigationBar.addComponent(navigationBarItems);
        navigationBar.setExpandRatio(navigationBarItems, 1.0f);
        Label userIdLabel = createIdLabel();
        navigationBar.addComponent(userIdLabel);
        navigationBar.setComponentAlignment(userIdLabel, Alignment.MIDDLE_RIGHT);
        navigationBar.setHeight(60, Sizeable.Unit.PIXELS);
        return navigationBar;
    }

    private static Button createNavigationButton(AbstractLayout layout, String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        // If you didn't choose Java 8 when creating the project, convert this
        // to an anonymous listener class
        button.addClickListener(event -> layout.getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    private static Label createIdLabel() {
        Label label = new Label(SecurityUtils.getLoggedInUserFullName()+", " + SecurityUtils.getGroups());
        return label;
    }
}
