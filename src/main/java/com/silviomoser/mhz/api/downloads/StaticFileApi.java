package com.silviomoser.mhz.api.downloads;

import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.StaticFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = StaticFileApi.API_CONTEXTROOT)
public class StaticFileApi extends CrudApi<StaticFile> {

    public static final String API_CONTEXTROOT = "/api/staticfiles";


}
