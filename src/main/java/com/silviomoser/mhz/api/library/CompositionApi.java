package com.silviomoser.mhz.api.library;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.api.core.CrudApi;
import com.silviomoser.mhz.data.Article;
import com.silviomoser.mhz.data.Composition;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.services.CompositionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/composition")
public class CompositionApi extends CrudApi<Composition> {

    @Autowired
    private CompositionService compositionService;

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Serach compositions")
    @JsonView(Views.Public.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Article.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @GetMapping(params = "searchTerm")
    public List<Composition> search(@RequestParam(name = "searchTerm") String searchTerm) {
        return compositionService.find(searchTerm);
    }
}
