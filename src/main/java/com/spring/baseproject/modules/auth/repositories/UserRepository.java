package com.spring.baseproject.modules.auth.repositories;

import com.spring.baseproject.modules.auth.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u " +
            "left join fetch u.role " +
            "where u.username = ?1")
    User getUserFetchRole(String username);

    @Modifying
    @Transactional
    @Query("update User u set u.lastActive = ?2 where u.id = ?1")
    void updateLastActive(String userID, Date lastActive);

    boolean existsByUsername(String username);

    @Query("select u.isBanned from User u where u.id = ?1")
    boolean isUserBanned(String userID);
}
