package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Composition;
import com.silviomoser.demo.repository.CompositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class CompositionService {

    @Autowired
    private CompositionRepository compositionRepository;

    public List<Composition> findByTitleContaining(String title) {
        return compositionRepository.findByTitleContaining(title);
    }

    public Composition add(Composition composition) throws ServiceException {
        compositionRepository.save(composition);
        log.info("Added/updated composition {}", composition);
        return composition;
    }

    public Composition update(Composition composition) throws ServiceException {
        try {
            compositionRepository.save(composition);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        log.info("Added/updated composition {}", composition);
        return composition;
    }

    public void delete(Composition composition) throws ServiceException {
        compositionRepository.delete(composition);
        log.info("Deleted person {}", composition);
    }

    public Collection<Composition> findAll() {
        return compositionRepository.findAll();
    }

}
