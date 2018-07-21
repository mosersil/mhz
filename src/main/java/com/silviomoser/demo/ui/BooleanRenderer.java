package com.silviomoser.demo.ui;

import com.vaadin.ui.renderers.AbstractRenderer;

/**
 * Created by silvio on 21.05.18.
 */
public class BooleanRenderer<T> extends AbstractRenderer<T, Boolean> {

    /**
     * simple boolean renderer that display true/false as icons
     */
    public BooleanRenderer() {
        super(Boolean.class);
    }

}
