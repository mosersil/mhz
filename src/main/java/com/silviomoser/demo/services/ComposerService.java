package com.silviomoser.demo.services;

import com.silviomoser.demo.data.Composer;
import com.silviomoser.demo.repository.ComposerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ComposerService {

    @Autowired
    private ComposerRepository composerRepository;


    public Collection<Composer> findAll() {
        return composerRepository.findAll();
    }

    public Composer add(Composer composer) throws ServiceException {
        Composer composer1 = composerRepository.save(composer);
        log.info("Added/updated composition {}", composer1);
        return composer1;
    }

    public Composer update(Composer composer) throws ServiceException {
        try {
            Composer composer1 = composerRepository.save(composer);
            log.info("Added/updated composition {}", composer1);
            return composer1;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void delete(Composer composer) throws ServiceException {
        composerRepository.delete(composer);
        log.info("Deleted composer {}", composer);
    }
}
