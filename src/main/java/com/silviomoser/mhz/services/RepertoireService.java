package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Repertoire;
import com.silviomoser.mhz.repository.RepertoireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepertoireService extends AbstractCrudService<Repertoire> {

    @Autowired
    RepertoireRepository repertoireRepository;

    public List<Repertoire> getAllCurrent() {
        return repertoireRepository.findCurrent();
    }

    public Repertoire get(String name) {
        return repertoireRepository.findOneByName(name);
    }
}
