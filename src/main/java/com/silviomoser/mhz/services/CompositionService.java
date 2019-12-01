package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Composition;
import com.silviomoser.mhz.data.Sheet;
import com.silviomoser.mhz.data.SheetUpload;
import com.silviomoser.mhz.repository.CompositionRepository;
import com.silviomoser.mhz.repository.SheetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private SheetRepository sheetRepository;

    @Autowired
    private FileBucketService fileBucketService;

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

    public void addSheet(SheetUpload sheetUpload) throws ServiceException {
        try {
            fileBucketService.putFile("sheets", sheetUpload.getTitle(), sheetUpload.getFile().getInputStream(), sheetUpload.getFile().getContentType());
            Sheet newSheet = new Sheet();
            newSheet.setLocation(sheetUpload.getTitle());
            newSheet.setTitle(sheetUpload.getTitle());

            Sheet savedSheet = sheetRepository.save(newSheet);


            Composition composition = compositionRepository.getOne((long) sheetUpload.getId());
            if (composition.getSheets()==null) {
                composition.setSheets(new HashSet<>(1));
            }
            composition.getSheets().add(savedSheet);
            compositionRepository.save(composition);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

}
