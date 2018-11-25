package com.silviomoser.demo.services;

import com.silviomoser.demo.data.AddressListEntry;
import com.silviomoser.demo.repository.AddressListRepository;
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
