package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.AddressListEntry;
import com.silviomoser.mhz.repository.AddressListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddresslistService {

    @Autowired
    private AddressListRepository addressListRepository;

    public List<AddressListEntry> generateAddressList(List<String> organization) {
        List<AddressListEntry> results = addressListRepository.findByOrganization(organization);
        return results.stream().distinct().collect(Collectors.toList());
    }

}
