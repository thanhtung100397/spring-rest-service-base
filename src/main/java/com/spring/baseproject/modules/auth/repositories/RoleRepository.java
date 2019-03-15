package com.spring.baseproject.modules.auth.repositories;

import com.spring.baseproject.modules.auth.models.dtos.RoleDto;
import com.spring.baseproject.modules.auth.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findFirstById(int id);

    @Query("select new com.spring.baseproject.modules.auth.models.dtos.RoleDto" +
            "(r.id, r.name, r.type) " +
            "from Role r " +
            "where r.id = ?1")
    RoleDto getRoleDto(Integer roleID);

    @Modifying
    @Transactional
    void deleteAllByIdIn(Set<Integer> ids);
}
