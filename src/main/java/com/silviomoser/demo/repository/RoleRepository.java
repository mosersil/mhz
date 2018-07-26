package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Role;
import com.silviomoser.demo.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Long> {

}
