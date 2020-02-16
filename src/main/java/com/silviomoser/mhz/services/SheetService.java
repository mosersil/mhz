package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Sheet;
import com.silviomoser.mhz.data.SheetUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SheetService extends AbstractCrudService<Sheet> {

    @Autowired
    private FileBucketService fileBucketService;


    public Sheet upload(SheetUpload sheetUpload) throws CrudServiceException {
        try {
            fileBucketService.putFile("sheets", sheetUpload.getTitle(), sheetUpload.getFile().getInputStream(), sheetUpload.getFile().getContentType());
            final Sheet newSheet = new Sheet();
            newSheet.setLocation(sheetUpload.getTitle());
            newSheet.setTitle(sheetUpload.getTitle());
            return add(newSheet);
        }
        catch (IOException | ServiceException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
