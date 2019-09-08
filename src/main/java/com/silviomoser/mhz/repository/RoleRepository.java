package com.silviomoser.mhz.repository;

import com.silviomoser.mhz.data.Role;
import com.silviomoser.mhz.data.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByType(RoleType roleType);

}
