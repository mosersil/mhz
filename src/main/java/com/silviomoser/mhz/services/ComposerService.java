package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Composer;
import com.silviomoser.mhz.repository.ComposerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ComposerService extends AbstractCrudService<Composer>{

    @Autowired
    private ComposerRepository composerRepository;


    public List<Composer> findByName(String name) {
        return (List<Composer>) composerRepository.findByNameContainingIgnoreCase(name);
    }

    public Composer findOneByName(String name) {
        return composerRepository.findOneByName(name);
    }
}
