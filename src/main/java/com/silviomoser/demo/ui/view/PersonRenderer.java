package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Person;
import com.vaadin.ui.renderers.AbstractRenderer;

public class PersonRenderer extends AbstractRenderer<Object, Person> {


    public PersonRenderer() {
        super(Person.class);
    }

}
