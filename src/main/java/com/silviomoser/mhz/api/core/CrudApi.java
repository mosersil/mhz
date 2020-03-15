package com.silviomoser.mhz.api.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.data.AbstractEntity;
import com.silviomoser.mhz.data.Article;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.services.AbstractCrudService;
import com.silviomoser.mhz.services.CrudServiceException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public abstract class CrudApi<T extends AbstractEntity> {




    @Autowired
    protected AbstractCrudService<T> service;

    @JsonView(Views.Public.class)
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get item by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Article.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public T getById(@PathVariable("id") Long id) throws CrudServiceException {
        return service.get(id);
    }

    @JsonView(Views.Public.class)
    @GetMapping
    public List<T> get(@RequestParam(value = "filter", required = false) String filter,
                       @RequestParam(value = "sortBy", required = false) String sortBy,
                       @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        if (pageNumber==null && pageSize == null) {
            return service.getAll(filter, sortBy);
        }
        else {
            return service.getAll(filter, pageNumber, pageSize, sortBy);
        }

    }


    @JsonView(Views.Public.class)
    @PostMapping
    public @ResponseBody
    ResponseEntity<T> post(@RequestBody T item) throws CrudServiceException {
        checkNotNull(item);
        log.debug("Try to create object {}", item);
        return new ResponseEntity<T>(service.add(item), HttpStatus.CREATED);
    }

    @JsonView(Views.Public.class)
    @PutMapping
    public @ResponseBody
    ResponseEntity<T> put(@RequestBody T item) throws CrudServiceException {
        checkNotNull(item);
        log.debug("Try to update object {}", item);
        return new ResponseEntity<T>(service.update(item), HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Long id) throws CrudServiceException {
        checkNotNull(id);
        service.delete(id);
    }


}
