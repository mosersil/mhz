package com.silviomoser.demo.api.internal;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.DocumentException;
import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.AddressListEntry;
import com.silviomoser.demo.data.CalendarEvent;
import com.silviomoser.demo.data.Person;
import com.silviomoser.demo.data.Views;
import com.silviomoser.demo.repository.AddressListRepository;
import com.silviomoser.demo.repository.CalendarEventRepository;
import com.silviomoser.demo.security.utils.SecurityUtils;
import com.silviomoser.demo.services.AddressListFormat;
import com.silviomoser.demo.services.AddresslistService;
import com.silviomoser.demo.services.CalendarService;
import com.silviomoser.demo.services.ServiceException;
import com.silviomoser.demo.utils.PdfBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.lastDayOfYear;

/**
 * Created by silvio on 29.07.18.
 */
@RestController
@Slf4j
public class InternalApi implements ApiController {

    @Autowired
    CalendarService calendarService;

    @Autowired
    private AddresslistService addresslistService;

    @PreAuthorize("hasAuthority('ROLE_DATAVIEWER')")
    @RequestMapping(value = URL_INTERNAL_CALENDAR, method = RequestMethod.GET)
    public ModelAndView getPDF(@RequestParam(name = "year", required = false) String year,
                                                      @RequestParam(name = "format") AddressListFormat format) {
        try {
            int currentYear = Integer.parseInt(year);
            log.debug("Assemble calendar for year {} in format {}", year, format);
            return new ModelAndView(format.name(), "entries", calendarService.getAllEventsForCurrentYear(currentYear));
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
            return new ModelAndView(format.name(), "entries", addresslistService.generateAddressList(organization));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ApiException(e.getMessage());
        }

    }


}
