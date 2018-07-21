package com.silviomoser.demo.views;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;

/**
 * Created by silvio on 10.05.18.
 */
@SpringView(name = LoginView.VIEW_NAME)
public class LoginView implements View {
    public static final String VIEW_NAME = "login";
}
