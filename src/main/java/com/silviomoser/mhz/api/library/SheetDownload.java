package com.silviomoser.mhz.api.library;

import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.Sheet;
import com.silviomoser.mhz.data.SheetUpload;
import com.silviomoser.mhz.data.type.FileType;
import com.silviomoser.mhz.services.CrudServiceException;
import com.silviomoser.mhz.services.FileBucketService;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.services.SheetService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SheetDownload {

    public static final String CONTEXTROOT = "/api/sheetdownload";

    @Autowired
    private FileBucketService fileBucketService;

    @Autowired
    private SheetService sheetService;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Download a sheet")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(path = CONTEXTROOT+"/{location}", method = GET)
    public ResponseEntity<InputStreamResource> publicDownload(@PathVariable(name = "location") String location) {

        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=sheet.pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.valueOf(FileType.PDF.getMime()))
                    .body(new InputStreamResource(new ByteArrayInputStream(fileBucketService.getFile("sheets", location))));
        } catch (ServiceException e) {
            throw new ApiException(e.getMessage());
        }
    }


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Upload a new sheet")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(path = CONTEXTROOT, method = POST)
    public Sheet uploadSheet(@ModelAttribute SheetUpload sheetUpload) throws CrudServiceException{
            return sheetService.upload(sheetUpload);
    }
}
