package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Composer;
import com.silviomoser.mhz.data.Composition;
import com.silviomoser.mhz.data.CompositionFormSubmition;
import com.silviomoser.mhz.repository.CompositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompositionService extends AbstractCrudService<Composition> {

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private ComposerService composerService;

    public List<Composition> find(String searchTerm) {
        Set<Composition> results = new HashSet<>();

        String[] searchStringArray = searchTerm.split(" ");

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

    public Composition create(CompositionFormSubmition compositionFormSubmition) throws ServiceException {

        final Composition composition = new Composition();
        composition.setTitle(compositionFormSubmition.getTitle());
        composition.setSubtitle(compositionFormSubmition.getSubtitle());
        composition.setDescription(compositionFormSubmition.getDescription());
        composition.setGenre(compositionFormSubmition.getGenre());

        final HashSet<Composer> composerArrayList = new HashSet<>();
        for (String s : compositionFormSubmition.getComposers()) {
            final Composer existingComposer = composerService.findOneByName(s);
            if (existingComposer != null) {
                composerArrayList.add(existingComposer);
            } else {
                Composer newComposer = new Composer();
                newComposer.setName(s);
                composerArrayList.add(composerService.addOrUpdate(newComposer));
            }
        }


        HashSet<Composer> arrangerArrayList = new HashSet<>();
        compositionFormSubmition.getArrangers().forEach(it -> {
            arrangerArrayList.add(composerService.findOneByName(it));
        });

        composition.setComposers(composerArrayList);

        return compositionRepository.save(composition);

    }
}
