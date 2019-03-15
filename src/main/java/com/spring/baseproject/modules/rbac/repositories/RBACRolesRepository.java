package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.auth.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RBACRolesRepository extends JpaRepository<Role, Integer> {
    Role findFirstById(int id);

    Set<Role> findAllByNameIn(Set<String> names);

    @Query("select r from Role r " +
            "left join fetch r.apis a " +
            "left join fetch a.roles " +
            "where r.id = ?1")
    Role getRoleWithFetchedApiWithFetchedRoles(int roleID);
}
