package com.silviomoser.mhz.api.core;

import com.fasterxml.jackson.annotation.JsonView;
import com.silviomoser.mhz.data.AbstractEntity;
import com.silviomoser.mhz.data.Views;
import com.silviomoser.mhz.services.AbstractCrudService;
import com.silviomoser.mhz.services.CrudServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class CrudApi<T extends AbstractEntity> {


    @Autowired
    protected AbstractCrudService<T> service;

    @JsonView(Views.Public.class)
    @GetMapping(value = "/{id}")
    public T getById(@PathVariable("id") Integer id) throws CrudServiceException {
        return service.get(id);
    }

    @JsonView(Views.Public.class)
    @GetMapping
    public List<T> get() {
        return service.getAll();
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
    @PostMapping(value = "/{id}")
    public @ResponseBody
    ResponseEntity<T> put(@RequestBody T item) throws CrudServiceException {
        checkNotNull(item);
        log.debug("Try to update object {}", item);
        return new ResponseEntity<T>(service.update(item), HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id) throws CrudServiceException {
        checkNotNull(id);
        service.delete(id);
    }

}
