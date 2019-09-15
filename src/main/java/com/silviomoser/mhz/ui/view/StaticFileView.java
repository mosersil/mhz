package com.silviomoser.mhz.ui.view;

import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Grid;

import java.time.LocalDateTime;

@SpringView(name = StaticFileView.VIEW_NAME)
public class StaticFileView extends AbstractCrudView<StaticFile> {

    public static final String VIEW_NAME = "files";

    @Override
    public void populateGrid(Grid<StaticFile> grid) {
        grid.addColumn(StaticFile::getId).setCaption("#").setWidth(70);
        grid.addColumn(StaticFile::getTitle).setCaption(i18Helper.getMessage("files_title"));
        grid.addColumn(StaticFile::getStaticFileCategory).setCaption(i18Helper.getMessage("files_category"));
        grid.addColumn(StaticFile::getDescription).setCaption(i18Helper.getMessage("files_description"));
        grid.addColumn(StaticFile::getFileType).setCaption(i18Helper.getMessage("files_type"));
    }

    @Override
    public StaticFile createNew() {
        return StaticFile.builder().person(SecurityUtils.getMe()).created(LocalDateTime.now()).build();
    }

    @Override
    public boolean applyFilter(StaticFile s, String filterString) {
        return s.getTitle().contains(filterString);
    }
}
