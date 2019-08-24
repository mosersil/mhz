package com.silviomoser.demo.ui.view;

import com.silviomoser.demo.data.Composer;
import com.silviomoser.demo.services.ComposerService;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ComposerPopup extends CustomField<Set<Composer>> {

    Grid<Composer> composerGrid = new Grid<Composer>();
    ComposerService composerService;

    @Override
    protected Component initContent() {
        FormLayout layout = new FormLayout();
        final Window window = new Window("Komponisten bearbeiten", layout);


        layout.addComponent(composerGrid);

        ComboBox comboBox = new ComboBox();
        comboBox.setItems(composerService.findAll());
        comboBox.setItemCaptionGenerator((ItemCaptionGenerator<Composer>) item -> item.getName());


        comboBox.setNewItemProvider(s -> {
            Composer composer = new Composer();
            composer.setName(s.toString());
            //Composer composer1 = composerService.create(composer);
            //comboBox.setData(composer1);
            //composerGrid.setItems(composerService.findAll());

            //composerGrid.getSelectedItems().add(composer1);
            //return Optional.of(composer1);
            return null;
        });

        layout.addComponent(comboBox);

        Button addButton = new Button("Hinzufügen", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                log.debug(comboBox.getValue().toString());
                Set<Composer> data = (Set<Composer>) composerGrid.getData();
                if (data==null) {
                    data = new HashSet<>();
                }
                data.add((Composer) comboBox.getValue());
            }
        });

        layout.addComponent(addButton);


        Button button = new Button("Komponisten bearbeiten", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().addWindow(window);
            }
        });
        window.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {

            }
        });
        return button;
    }

    @Override
    protected void doSetValue(Set<Composer> value) {
        if (value!=null) {
            composerGrid.setItems(value);
        } else {
            composerGrid.setItems(new HashSet<>());
        }

    }

    @Override
    public Set<Composer> getValue() {
        return (Set<Composer>) composerGrid.getData();
    }

    public void setComposerService(ComposerService composerService) {
        this.composerService = composerService;
    }
}
