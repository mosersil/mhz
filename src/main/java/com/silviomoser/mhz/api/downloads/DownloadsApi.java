package com.silviomoser.mhz.api.downloads;

import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.StaticFile;
import com.silviomoser.mhz.security.utils.SecurityUtils;
import com.silviomoser.mhz.services.AbstractFileService;
import com.silviomoser.mhz.services.CrudServiceException;
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
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@Slf4j
@RequestMapping(value = DownloadsApi.API_CONTEXTROOT)
public class DownloadsApi extends CrudApi<StaticFile> {

    public static final String API_CONTEXTROOT = "/api/staticfiles";

    @Autowired
    private  FileService legacyFileService;

    @Autowired
    private AbstractFileService fileService;

    @Autowired
    private I18Helper i18Helper;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Download a public static file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = "/api/publicdownload", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> publicDownload(@RequestParam(name = "id") Long id) throws CrudServiceException, ServiceException {

        final StaticFile staticFile = getById(id);

        if (staticFile.getRole() != null) {
            log.warn(String.format("rejected attempt to download file id '%s' (unauthorized)", id));
            throw new ApiException(i18Helper.getMessage("unauthorized"), HttpStatus.UNAUTHORIZED);
        }

        final ByteArrayInputStream bis = new ByteArrayInputStream(fileService.getFile("downloads", staticFile.getLocation()));
        return downloadResponse(bis, staticFile);

    }

    @RequestMapping(value = "/api/securedownload", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> secureDownload(@RequestParam(name = "id") Long id) throws CrudServiceException, ServiceException {

        final StaticFile staticFile = getById(id);

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
}
