package com.spring.baseproject.modules.auth.repositories;

import com.spring.baseproject.modules.auth.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findFirstById(int id);
}
