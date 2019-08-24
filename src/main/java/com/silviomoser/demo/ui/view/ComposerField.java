package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Composer;
import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ComposerField extends VerticalLayout
        implements HasValue<Set<Composer>> {

    private final ComboBox<Composer> composerSelect;
    private final Button updateButton;

    private Grid<Composer> grid = new Grid<>();

    private Set<Composer> composerSet;

    public ComposerField() {

        if (composerSet == null) {
            composerSet = new HashSet<>(0);
        }
        grid.setItems(composerSet);


        composerSelect = new ComboBox<>();
        composerSelect.setCaption("Set composer");
        composerSelect.setTextInputAllowed(true);
        composerSelect.clear();
        composerSelect.setWidth(100, Unit.PERCENTAGE);

        grid.addColumn(Composer::getId).setCaption("#").setWidth(70);
        grid.addColumn(Composer::getName).setCaption("Name").setWidth(70);


        updateButton = new Button("Update");
        updateButton.setEnabled(true);
        updateButton.setWidth(100, Unit.PERCENTAGE);

        addComponents(grid, composerSelect, updateButton);
        setWidth(100, Unit.PERCENTAGE);
    }


    @Override
    public void setValue(Set<Composer> value) {
        composerSet = value;
        grid.setItems(composerSet);
    }

    @Override
    public Set<Composer> getValue() {
        return composerSet;
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {

    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    /*
    @Override
    public Registration addValueChangeListener(ValueChangeListener<Set<Composer>> listener) {
        return null;
    }
    */

    @Override
    public Registration addValueChangeListener(
            ValueChangeListener<Set<Composer>> listener) {
        return addListener(ValueChangeEvent.class, listener,
                ValueChangeListener.VALUE_CHANGE_METHOD);
    }

    public void setItems(Collection<Composer> items) {
        composerSelect.setItems(items);

    }
}
