package com.silviomoser.mhz.api.library;

import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.Repertoire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/api/repertoire")
public class RepertoireApi extends CrudApi<Repertoire> {
}
