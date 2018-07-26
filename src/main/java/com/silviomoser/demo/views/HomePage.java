package com.silviomoser.demo.views;

import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.security.SecurityUserDetails;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.utils.ExampleUtil;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by silvio on 10.05.18.
 */
@SpringView(name = "")
public class HomePage extends VerticalLayout implements View {


    @PostConstruct
    void init() {

        Panel homePanel = new Panel("MHZ Admin Bereich");
        homePanel.setHeight(100.0f, Unit.PERCENTAGE);
        homePanel.setSizeFull();


        Label label = new Label();
        label.setContentMode(ContentMode.HTML);
        label.setValue(ExampleUtil.home());
        label.setWidth(100, Unit.PERCENTAGE);

        homePanel.setContent(label);

        addComponent(NavigationBar.buildNavigationBar(this));
        addComponent(homePanel);

    }

}
