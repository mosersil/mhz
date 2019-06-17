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

    public Composer create(Composer composer) {
        return composerRepository.save(composer);
    }
}
