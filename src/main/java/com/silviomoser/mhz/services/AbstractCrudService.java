package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.AbstractEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Slf4j
public abstract class AbstractCrudService<T extends AbstractEntity> {

    @Autowired
    private JpaRepository<T, Long> repository;

    public T addOrUpdate(T item) throws ServiceException {
        final T item1 = repository.save(item);
        log.info("Saved item {} of type {}", item1.getId(), item1.getClass().getName());
        return item1;
    }

    public T update(T item) throws ServiceException {
        try {
            repository.getOne(item.getId());
        } catch (EntityNotFoundException e) {
            throw new ServiceException(String.format("Item %s of type %s does not exist", item.getId(), item.getClass().getName()));
        }
        log.info("updating item {} of type {}", item.getId(), item.getClass().getName());
        return repository.save(item);
    }

    public T get(long id) throws ServiceException {
        final T item = repository.getOne(id);
        log.info("Loaded item {} of type {}", item.getId(), item.getClass().getName());
        return item;
    }

    public void delete(T item) throws ServiceException {
        repository.delete(item);
        log.info("Deleted item {} of type {}", item.getId(), item.getClass().getName());
    }

    public List<T> getAll() {
        final List<T> results = repository.findAll();
        return results;
    }

}
