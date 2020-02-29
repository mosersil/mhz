package com.silviomoser.mhz.api.downloads;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.data.StaticFileUpload;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.data.type.FileType;
import com.silviomoser.mhz.data.type.StaticFileCategory;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import com.silviomoser.mhz.services.CrudServiceException;
import com.silviomoser.mhz.services.FileBucketService;
import com.silviomoser.mhz.services.ServiceException;
import com.silviomoser.mhz.services.StaticFileService;
import com.silviomoser.mhz.ui.i18.I18Helper;
import com.silviomoser.mhz.utils.FileNameUtils;
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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@Slf4j
public class StaticFileDownload {

    @Autowired
    private StaticFileService legacyFileService;

    @Autowired
    private FileBucketService fileService;


    @Autowired
    private I18Helper i18Helper;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Download a public static file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/api/publicdownload/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> publicDownload(@PathVariable Long id) throws CrudServiceException, ServiceException {

        final StaticFile staticFile = legacyFileService.get(id);

        if (staticFile.getRole() != null) {
            log.warn(String.format("rejected attempt to download file id '%s' (unauthorized)", id));
            throw new ApiException(i18Helper.getMessage("unauthorized"), HttpStatus.UNAUTHORIZED);
        }

        final ByteArrayInputStream bis = new ByteArrayInputStream(fileService.getFile("downloads", staticFile.getLocation()));
        return downloadResponse(bis, staticFile);

    }

    @JsonView(Views.Public.class)
    @RequestMapping(value = "/api/securedownload/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> secureDownload(@PathVariable Long id) throws CrudServiceException, ServiceException {

        final StaticFile staticFile = legacyFileService.get(id);

        if (staticFile.getRole() != null) {
            if (!SecurityUtils.hasRole(staticFile.getRole().getType())) {
                throw new ApiException(i18Helper.getMessage("unauthorized"), HttpStatus.UNAUTHORIZED);
            }
        }

        final ByteArrayInputStream bis = new ByteArrayInputStream(fileService.getFile("downloads", staticFile.getLocation()));
        return downloadResponse(bis, staticFile);
    }


    //TODO: To be removed
    @RequestMapping(value = "/public/api/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> publicDownloadLegacy(@RequestParam(name = "id", required = true) Long id) {

        try {
            final StaticFile staticFile = legacyFileService.get(id);
            if (staticFile.getRole() != null) {
                log.warn(String.format("rejected attempt to download file id '%s' (unauthorized)", id));
                throw new ApiException(i18Helper.getMessage("unauthorized"), HttpStatus.UNAUTHORIZED);
            }
            try {
                final ByteArrayInputStream bis = legacyFileService.download(staticFile);
                return downloadResponse(bis, staticFile);
            } catch (CrudServiceException e) {
                throw new ApiException(e.getMessage());
            }

        } catch (CrudServiceException e) {
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


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Upload a new sheet")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(path = "/api/uploadstaticfile", method = RequestMethod.POST)
    public StaticFile uploadFile(@ModelAttribute StaticFileUpload staticFileUpload) throws IOException, ServiceException, CrudServiceException {

        final String fileLocation = FileNameUtils.normalizeFileName(staticFileUpload.getTitle());
        fileService.putFile("downloads", fileLocation, staticFileUpload.getFile().getInputStream(), staticFileUpload.getFile().getContentType(), true);

        final StaticFile staticFile = StaticFile.builder()
                .title(staticFileUpload.getTitle())
                .created(LocalDateTime.now())
                .fileType(FileType.PDF)
                .location(fileLocation)
                .staticFileCategory(StaticFileCategory.GENERIC)
                .description(staticFileUpload.getTitle())
                .build();
        return legacyFileService.add(staticFile);

    }

}
