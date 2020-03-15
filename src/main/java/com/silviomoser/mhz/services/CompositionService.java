package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Composition;
import com.silviomoser.mhz.repository.CompositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CompositionService extends AbstractCrudService<Composition> {

    @Autowired
    private CompositionRepository compositionRepository;


    public List<Composition> find(String searchTerm) {
        final Set<Composition> results = new HashSet<>();

        final String[] searchStringArray = searchTerm.split(" ");

        Arrays.stream(searchStringArray).forEach(it -> {
            if (it.length() > 3) {
                results.addAll(compositionRepository.findByTitleContainingIgnoreCase(it));
            }
        });

        Arrays.stream(searchStringArray).forEach(it -> {
            if (it.length() > 3) {
                results.addAll(compositionRepository.findByComposersNameIsContainingIgnoreCase(it));
            }
        });

        return new ArrayList<>(results);
    }

}
