package com.silviomoser.demo.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.UI;

/**
 * Created by silvio on 04.04.18.
 */
@SpringUI(path = "/app")
@SpringViewDisplay
@Theme("valo")
@VaadinServletConfiguration(ui = VaadinUI.class, productionMode = true)
public class VaadinUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

    }

}

