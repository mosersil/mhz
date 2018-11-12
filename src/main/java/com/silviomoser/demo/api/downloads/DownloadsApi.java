package com.silviomoser.demo.api.downloads;

import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.services.FileService;
import com.silviomoser.demo.ui.i18.I18Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class DownloadsApi {

    @Autowired
    FileService fileService;
    @Autowired
    private I18Helper i18Helper;

    @RequestMapping(value = "/public/api/download")
    public ResponseEntity<InputStreamResource> publicDownload(@RequestParam(name = "id", required = true) Long id) {

        StaticFile staticFile = fileService.findById(id);

        if (staticFile.getRole() != null) {
            throw new ApiException(i18Helper.getMessage("Keine Berechtigung für diese Resource"), HttpStatus.UNAUTHORIZED);
        }

        if (staticFile != null) {
            try {
                final ByteArrayInputStream bis = fileService.download(staticFile);
                return downloadResponse(bis, staticFile);
            } catch (IOException e) {
                throw new ApiException(e.getMessage());
            }
        }
        throw new ApiException("Invalid download", HttpStatus.INTERNAL_SERVER_ERROR);
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
