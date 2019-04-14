package com.silviomoser.demo.api.internal;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.StaticFile;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.data.type.AddressListFormat;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.services.AddresslistService;
import com.silviomoser.demo.services.CalendarService;
import com.silviomoser.demo.services.FileService;
import com.silviomoser.demo.services.PersonService;
import com.silviomoser.demo.services.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.silviomoser.demo.utils.StringUtils.isBlank;

/**
 * Created by silvio on 29.07.18.
 */
@RestController
@Slf4j
public class InternalApi implements ApiController {

    @Autowired
    CalendarService calendarService;

    @Autowired
    private FileService fileService;

    @Autowired
    private AddresslistService addresslistService;

    @Autowired
    private PersonService personService;

    @PreAuthorize("hasAuthority('ROLE_DATAVIEWER')")
    @RequestMapping(value = URL_INTERNAL_CALENDAR, method = RequestMethod.GET)
    public ModelAndView getPDF(@RequestParam(name = "year", required = false) String year,
                                                      @RequestParam(name = "format") AddressListFormat format) {
        try {
            int currentYear = Integer.parseInt(year);
            log.debug("Assemble calendar for year {} in format {}", year, format);
            ModelAndView modelAndView = new ModelAndView(format.name(), "entries", calendarService.getAllEventsForCurrentYear(currentYear));
            modelAndView.addObject("title", "Jahresprogramm " + year);
            return modelAndView;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_DATAVIEWER')")
    @RequestMapping(value = URL_INTERNAL_ADDRESSLIST, method = RequestMethod.GET)
    public ModelAndView getAddressList(@RequestParam(name = "organization") String organization,
                                       @RequestParam(name = "format") AddressListFormat format) {
        try {
            log.debug("Assemble document {} in format {}", organization, format);
            ModelAndView modelAndView =  new ModelAndView(format.name(), "entries", addresslistService.generateAddressList(organization));
            modelAndView.addObject("title", "Adressliste " + organization);
            return modelAndView;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(e.getMessage());
        }

    }

    @JsonView(Views.Public.class)
    @RequestMapping(value = URL_INTERNAL_FILES, method = RequestMethod.GET)
    public List<StaticFile> getStaticFiles(@RequestParam(name="staticFileCategory", required = false) String category) {
        try {
            final List availableFiles = fileService.getFiles(category);
            log.debug("returning " + availableFiles);
            return availableFiles;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(e.getMessage());
        }
    }



    @RequestMapping(value = URL_INTERNAL_ADDRESS, method = RequestMethod.POST)
    public void postAddress(@RequestBody PostAddressForm postAddressForm) throws ServiceException {
        final Person person = SecurityUtils.getMe();
        person.setGender(postAddressForm.getGender());
        person.setCompany(isBlank(postAddressForm.getCompany()) ? null : postAddressForm.getCompany());
        person.setTitle(isBlank(postAddressForm.getTitle()) ? null : postAddressForm.getTitle());
        person.setFirstName(isBlank(postAddressForm.getFirstName())? null : postAddressForm.getFirstName());
        person.setLastName(isBlank(postAddressForm.getLastName())? null : postAddressForm.getLastName());
        person.setAddress1(isBlank(postAddressForm.getAddress1())? null : postAddressForm.getAddress1());
        person.setAddress2(isBlank(postAddressForm.getAddress2())? null : postAddressForm.getAddress2());
        person.setZip(isBlank(postAddressForm.getZip())? null : postAddressForm.getZip());
        person.setCity(isBlank(postAddressForm.getCity())? null : postAddressForm.getCity());
        personService.update(person);
    }


    @RequestMapping(value = URL_INTERNAL_CONTACTDETAILS, method = RequestMethod.POST)
    public void postAddress(@RequestBody PostContactForm postAddressForm) throws ServiceException {
        final Person person = SecurityUtils.getMe();
        person.setEmail(isBlank(postAddressForm.getEmail()) ? null : postAddressForm.getEmail());
        person.setMobile(isBlank(postAddressForm.getMobile()) ? null : postAddressForm.getMobile());
        person.setLandline(isBlank(postAddressForm.getLandline()) ? null : postAddressForm.getLandline());
        person.getUser().setUsername(isBlank(postAddressForm.getEmail()) ? null : postAddressForm.getEmail());
        personService.update(person);
    }

    @RequestMapping(value = URL_INTERNAL_BIRTHDAY, method = RequestMethod.POST)
    public void postBirthDay(@RequestBody BirthdayForm birthdayForm) throws ServiceException {
        final Person person = SecurityUtils.getMe();
        person.setBirthDate(birthdayForm.getDate());
        personService.update(person);
    }



    @PreAuthorize("hasAuthority('ROLE_DATAVIEWER')")
    @RequestMapping(value = URL_INTERNAL_DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> staticFileDownload(@RequestParam(name = "id") Long id) {
        final StaticFile staticFile = fileService.findById(id);

        if (staticFile != null) {
            try {
                final ByteArrayInputStream bis = fileService.download(staticFile);
                return downloadResponse(bis, staticFile);
            } catch (ServiceException e) {
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
