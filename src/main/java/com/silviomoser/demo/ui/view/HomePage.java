package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.ui.NavigationBar;
import com.silviomoser.demo.utils.ExampleUtil;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import javax.annotation.PostConstruct;

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
