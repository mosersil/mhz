package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.AddressListEntry;
import com.silviomoser.mhz.repository.AddressListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddresslistService {

    @Autowired
    AddressListRepository addressListRepository;

    public List<AddressListEntry> generateAddressList(String organization) {
        return addressListRepository.findByOrganization(organization);
    }

}