package com.silviomoser.mhz.services;

import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.type.RoleType;
import com.silviomoser.mhz.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService extends AbstractCrudService<Role> {

    @Autowired
    private RoleRepository roleRepository;

    public Role getByRoleType(RoleType roleType) {
        final Optional<Role> optionalRole = roleRepository.findByType(roleType);
        return optionalRole.get();
    }
}
