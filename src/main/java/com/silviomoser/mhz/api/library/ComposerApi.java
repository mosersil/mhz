package com.silviomoser.mhz.api.library;

import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.Composer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = ComposerApi.API_CONTEXTROOT)
public class ComposerApi extends CrudApi<Composer> {

    public static final String API_CONTEXTROOT = "/api/composer";
}
