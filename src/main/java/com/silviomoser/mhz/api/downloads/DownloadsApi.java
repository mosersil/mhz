package com.silviomoser.mhz.api.downloads;

import com.silviomoser.mhz.api.core.ApiController;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.services.FileService;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.ui.i18.I18Helper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@Slf4j
public class DownloadsApi implements ApiController {

    @Autowired
    FileService fileService;
    @Autowired
    private I18Helper i18Helper;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Download a public static file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_DOWNLOADS, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> publicDownload(@RequestParam(name = "id", required = true) Long id) {

        try {
            final StaticFile staticFile = fileService.get(id);
            if (staticFile.getRole() != null) {
                log.warn(String.format("rejected attempt to download file id '%s' (unauthorized)", id));
                throw new ApiException(i18Helper.getMessage("unauthorized"), HttpStatus.UNAUTHORIZED);
            }
            try {
                final ByteArrayInputStream bis = fileService.download(staticFile);
                return downloadResponse(bis, staticFile);
            } catch (ServiceException e) {
                throw new ApiException(e.getMessage());
            }

        } catch (ServiceException e) {
            throw new ApiException("Invalid download", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private ResponseEntity<InputStreamResource> downloadResponse(ByteArrayInputStream bis, StaticFile staticFile) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("inline; filename=%s", staticFile.getLocation()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.valueOf(staticFile.getFileType().getMime()))
                .body(new InputStreamResource(bis));
    }
}
