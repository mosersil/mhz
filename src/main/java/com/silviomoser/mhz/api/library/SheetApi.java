package com.silviomoser.mhz.api.library;

import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.Sheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = SheetApi.API_CONTEXTROOT)
public class SheetApi extends CrudApi<Sheet> {

    public static final String API_CONTEXTROOT = "/api/sheet";

}
