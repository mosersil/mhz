package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Sheet;
import com.silviomoser.mhz.data.SheetUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SheetService extends AbstractCrudService<Sheet> {


    private AbstractFileService sheetFileService = new AbstractFileService() {};


    public Sheet upload(SheetUpload sheetUpload) throws CrudServiceException {
        try {
            sheetFileService.putFile("sheets", sheetUpload.getTitle(), sheetUpload.getFile().getInputStream(), sheetUpload.getFile().getContentType(), true);
            final Sheet newSheet = Sheet.builder()
                    .title(sheetUpload.getTitle())
                    .location(sheetUpload.getTitle())
                    .build();
            return add(newSheet);
        }
        catch (IOException | ServiceException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
