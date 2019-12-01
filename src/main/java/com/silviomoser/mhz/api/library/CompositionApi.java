package com.silviomoser.mhz.api.library;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.ApiController;
import com.silviomoser.mhz.api.core.ApiException;
import com.silviomoser.mhz.data.Composer;
import com.silviomoser.mhz.data.Composition;
import com.silviomoser.mhz.data.Repertoire;
import com.silviomoser.mhz.data.SheetUpload;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.data.type.FileType;
import com.silviomoser.mhz.services.ComposerService;
import com.silviomoser.mhz.services.CompositionService;
import com.silviomoser.mhz.services.FileBucketService;
import com.silviomoser.mhz.services.RepertoireService;
import com.silviomoser.mhz.services.ServiceException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.silviomoser.mhz.utils.StringUtils.isBlank;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Slf4j
public class CompositionApi implements ApiController {

    @Autowired
    private ComposerService composerService;

    @Autowired
    private CompositionService compositionService;

    @Autowired
    private FileBucketService fileBucketService;

    @Autowired
    private RepertoireService repertoireService;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List all compositions")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Composition.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_COMPOSITION, method = GET)
    public List<Composition> list(@RequestParam(name = "searchTerm", required = false) String searchTerm) {
        if (isBlank(searchTerm)) {
            return compositionService.getAll();
        } else {
            return compositionService.find(searchTerm);
        }
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Get composition by id")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Composition.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_COMPOSITION + "/{id}", method = GET)
    public Composition get(@PathVariable Long id) {
        try {
            return compositionService.get(id);
        } catch (ServiceException e) {
            throw new ApiException(e.getMessage(), e);
        }
    }


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List all composers")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Composition.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_COMPOSER, method = GET)
    public List<Composer> composers(@RequestParam(name = "searchTerm", required = false) String searchTerm) {
        if (isBlank(searchTerm)) {
            return composerService.getAll();
        } else {
            return composerService.findByName(searchTerm);
        }
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Create new composition")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success", response = Composition.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_COMPOSITION, method = RequestMethod.POST)
    public Composition create(@Valid @RequestBody Composition composition) {
        try {
            return compositionService.addOrUpdate(composition);
        } catch (ServiceException e) {
            log.warn(e.getMessage(), e);
            throw new ApiException(e.getMessage(), e);
        }
    }


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Download a public static file")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(path = URL_SHEET + "/{location}", method = GET)
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
    @RequestMapping(path = URL_SHEET, method = POST)
    public void uploadSheet(@ModelAttribute SheetUpload sheetUpload) throws ApiException{
        try {
            compositionService.addSheet(sheetUpload);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @RequestMapping(method = GET, path = URL_SAMPLE + "/{location}")
    @ResponseBody
    public void stream(HttpServletResponse response, @PathVariable String location) {
        try {
            OutputStream out = response.getOutputStream();
            InputStream is = fileBucketService.getFileStream("samples", location);
            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = is.read(buffer);
                if (bytesRead == -1)
                    break;
                out.write(buffer, 0, bytesRead);
            }

        } catch (ServiceException | IOException e) {
            throw new ApiException(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "List all repertoires")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Composition.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_REPERTOIRE, method = GET)
    public List<Repertoire> repertoires() {
            return repertoireService.getAllCurrent();
    }


    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Get repertoire by name")
    @JsonView(Views.RepertoireSummary.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Composition.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(value = URL_REPERTOIRE+"/{name}", method = GET)
    public Repertoire repertoires(@PathVariable String name) {
            return repertoireService.get(name);

    }




}
