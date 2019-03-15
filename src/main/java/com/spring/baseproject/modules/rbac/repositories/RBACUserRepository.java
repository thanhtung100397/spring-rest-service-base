package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.auth.models.entities.User;
import com.spring.baseproject.modules.rbac.models.dtos.RBACAuthorizationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RBACUserRepository extends JpaRepository<User, String> {
    Set<User> findAllByUsernameIn(Set<String> username);

    @Query("select new com.spring.baseproject.modules.rbac.models.dtos.RBACAuthorizationResult(r.type, a.id, u.isBanned) " +
            "from User u " +
            "left join u.role r " +
            "left join Api a on (a.method.name = ?2 and a.route = ?3 and a member of r.apis) " +
            "where u.id = ?1")
    RBACAuthorizationResult authorizeUser(String id, String method, String route);
}