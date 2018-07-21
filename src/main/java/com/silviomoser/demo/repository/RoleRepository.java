package com.silviomoser.demo.repository;

import com.silviomoser.demo.data.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r where :user member r.users")
    List<Role> findByUserId(Long userId);
}
