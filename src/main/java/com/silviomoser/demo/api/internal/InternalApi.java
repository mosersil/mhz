package com.silviomoser.demo.api.internal;

import com.silviomoser.demo.api.core.ApiController;
import com.silviomoser.demo.api.core.ApiException;
import com.silviomoser.demo.data.type.AddressListFormat;
import com.silviomoser.demo.services.AddresslistService;
import com.silviomoser.demo.services.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
