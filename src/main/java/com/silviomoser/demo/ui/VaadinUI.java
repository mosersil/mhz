package com.silviomoser.demo.ui;

import com.silviomoser.demo.data.type.RoleType;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.ui.view.AccountCrud;
import com.silviomoser.demo.ui.view.ArticleCrud;
import com.silviomoser.demo.ui.view.CalendarCrud;
import com.silviomoser.demo.ui.view.CompositionCrud;
import com.silviomoser.demo.ui.view.DefaultView;
import com.silviomoser.demo.ui.view.MembershipCrud;
import com.silviomoser.demo.ui.view.PersonCrud;
import com.silviomoser.demo.ui.view.StaticFileView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by silvio on 04.04.18.
 */
@SpringUI(path = "/app")
@SpringViewDisplay
@Theme("valo")
@VaadinServletConfiguration(ui = VaadinUI.class, productionMode = true)
public class VaadinUI extends UI {

    @Autowired
    SpringNavigator springNavigator;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Label title = new Label("Administration");
        title.addStyleName(ValoTheme.MENU_TITLE);

        CssLayout menu = new CssLayout(title);
        if (SecurityUtils.hasRole(RoleType.PERSMGR)) {
            Button personCrud = new Button("Adressen", e -> getNavigator().navigateTo(PersonCrud.VIEW_NAME));
            personCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
            Button groupCrud = new Button("Gruppen", e -> getNavigator().navigateTo(MembershipCrud.VIEW_NAME));
            groupCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
            menu.addComponents(personCrud, groupCrud);
        }
        if (SecurityUtils.hasRole(RoleType.CONTMGR)) {
            Button articleCrud = new Button("News Artikel", e -> getNavigator().navigateTo(ArticleCrud.VIEW_NAME));
            articleCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
            Button calendarCrud = new Button("Kalender", e -> getNavigator().navigateTo(CalendarCrud.VIEW_NAME));
            calendarCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);

            Button compositionCrud = new Button("Noten", e -> getNavigator().navigateTo(CompositionCrud.VIEW_NAME));
            calendarCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
            menu.addComponents(articleCrud, calendarCrud, compositionCrud);
        }
        if (SecurityUtils.hasRole(RoleType.USRMGR)) {
            Button accountsCrud = new Button("Accounts", e -> getNavigator().navigateTo(AccountCrud.VIEW_NAME));
            accountsCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);

            Button downloadsCrud = new Button("Downloads", e -> getNavigator().navigateTo(StaticFileView.VIEW_NAME));
            downloadsCrud.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
            menu.addComponents(accountsCrud, downloadsCrud);
        }

        Button logoutButton = new Button("Logout", e -> {
            getUI().getSession().close();
            getUI().getPage().setLocation("/logout");
        });
        logoutButton.addStyleNames(ValoTheme.BUTTON_LINK, ValoTheme.MENU_ITEM);
        menu.addComponent(logoutButton);

        menu.setSizeFull();
        menu.addStyleName(ValoTheme.MENU_ROOT);

        VerticalLayout viewContainer = new VerticalLayout();

        HorizontalLayout mainLayout = new HorizontalLayout(menu, viewContainer);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(menu, 2);
        mainLayout.setExpandRatio(viewContainer, 8);
        setContent(mainLayout);

        getNavigator().addView("", new DefaultView());

        springNavigator.init(this, viewContainer);
    }

}

