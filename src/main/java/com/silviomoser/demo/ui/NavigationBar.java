package com.silviomoser.demo.ui;

import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.views.CalendarView;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by silvio on 10.05.18.
 */
public class NavigationBar {

    public static Layout buildNavigationBar(AbstractLayout layout) {

        final HorizontalLayout navigationBar = new HorizontalLayout();
        //navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton(layout, "Home", ""));
        navigationBar.addComponent(createNavigationButton(layout, "Kalender", CalendarView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton(layout, "Vereinsmitglieder", "notImplementedYet"));
        navigationBar.addComponent(createNavigationButton(layout, "Notenverwaltung", "notImplementedYet"));

        HorizontalLayout navigationBarLayout = new HorizontalLayout();
        navigationBarLayout.addComponent(navigationBar);
        navigationBarLayout.setExpandRatio(navigationBar, 1.0f);
        Label userIdLabel = createIdLabel();
        navigationBarLayout.addComponent(userIdLabel);
        navigationBarLayout.setComponentAlignment(userIdLabel, Alignment.MIDDLE_RIGHT);
        return navigationBarLayout;

        //return navigationBar;
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
