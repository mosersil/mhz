package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.AbstractEntity;
import com.silviomoser.mhz.services.error.ErrorType;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.silviomoser.mhz.services.error.ErrorType.ALREADY_EXIST;
import static com.silviomoser.mhz.services.error.ErrorType.NOT_FOUND;
import static com.silviomoser.mhz.utils.StringUtils.isBlank;


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
        } else {
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

    public List<T> getAll(String filter, String sortBy) {

        if (isBlank(filter)) {
            if (isBlank(sortBy)) {
                return getAll();
            }
        }

        final Node rootNode = new RSQLParser().parse(filter);
        final Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());

        if (isBlank(sortBy)) {
            return ((JpaSpecificationExecutor<T>) repository).findAll(spec);
        } else {
            return ((JpaSpecificationExecutor<T>) repository).findAll(spec, Sort.by(Sort.Direction.ASC, sortBy));
        }
    }

    public List<T> getAll(String filter, int pageNumber, int pageSize, String sortBy) {

        if (isBlank(sortBy)) {
            sortBy = "id";
        }

        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());

        if (isBlank(filter)) {
            return repository.findAll(pageable).getContent();
        }

        final Node rootNode = new RSQLParser().parse(filter);
        final Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());

        return ((JpaSpecificationExecutor<T>) repository).findAll(spec, pageable).getContent();

    }

}
