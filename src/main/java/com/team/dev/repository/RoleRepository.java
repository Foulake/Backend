package com.team.dev.repository;

import com.team.dev.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Object> findByRoleName(String user);
}
