package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.AbstractEntity;
import com.silviomoser.mhz.services.error.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.silviomoser.mhz.services.error.ErrorType.ALREADY_EXIST;
import static com.silviomoser.mhz.services.error.ErrorType.NOT_FOUND;


@Slf4j
public abstract class AbstractCrudService<T extends AbstractEntity> {

    @Autowired
    private JpaRepository<T, Long> repository;

    @Deprecated
    public T addOrUpdate(T item) {
        final T item1 = repository.save(item);
        log.info("Saved item {} of type {}", item1.getId(), item1.getClass().getName());
        return item1;
    }

    public T add(T item) throws CrudServiceException {
        if (item.getId() != null && repository.existsById(item.getId())) {
            log.warn("Item with id {} does already exist", item.getId());
            throw new CrudServiceException(ALREADY_EXIST);
        }
        final T item1 = repository.save(item);
        log.info("Saved item {} of type {}", item1.getId(), item1.getClass().getName());
        return item1;
    }

    public T update(T item) throws CrudServiceException {
        if (repository.existsById(item.getId())) {
            T updateItem = repository.save(item);
            log.info("updated item {} of type {}", updateItem.getId(), updateItem.getClass().getName());
            return updateItem;
        }
        throw new CrudServiceException(ErrorType.NOT_FOUND);
    }


    public T get(long id) throws CrudServiceException {
        if (repository.existsById(id)) {
            final Optional<T> optionalItem = repository.findById(id);
            return optionalItem.isPresent() ? optionalItem.get() : null;
        }
        else {
            throw new CrudServiceException(NOT_FOUND);
        }
    }

    public void delete(T item) throws CrudServiceException {
        checkNotNull(item);
        delete(item.getId());
    }

    public void delete(long id) throws CrudServiceException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Deleted item {}", id);
        } else {
            log.warn("Could not find item with id {}", id);
            throw new CrudServiceException(NOT_FOUND);
        }
    }

    public List<T> getAll() {
        final List<T> results = repository.findAll();
        return results;
    }

}
