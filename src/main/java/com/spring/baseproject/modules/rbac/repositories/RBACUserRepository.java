package com.spring.baseproject.modules.rbac.repositories;

import com.spring.baseproject.modules.auth.models.entities.User;
import com.spring.baseproject.modules.rbac.models.dtos.RBACAuthorizationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RBACUserRepository extends JpaRepository<User, String> {
    Set<User> findAllByUsernameIn(Set<String> username);

    @Query("select new com.spring.baseproject.modules.rbac.models.dtos.RBACAuthorizationResult(r.type, a.id, a1.id, u.isBanned) " +
            "from User u " +
            "left join u.role r " +
            "left join Api a on (a.method.name = ?2 and a.route = ?3 ) " +
            "left join Api a1 on (a1.id = a.id and a1 member of r.apis) " +
            "where u.id = ?1")
    RBACAuthorizationResult authorizeUser(String id, String method, String route);
}