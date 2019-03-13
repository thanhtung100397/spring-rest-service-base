package com.spring.baseproject.modules.demo_auth.repositories;

import com.spring.baseproject.modules.auth.models.entities.User;
import com.spring.baseproject.modules.demo_auth.models.dtos.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<User, String> {
    @Query("select new com.spring.baseproject.modules.demo_auth.models.dtos.UserDto" +
            "(u.id, u.username, u.password, u.isBanned, u.lastActive," +
            "r.id, r.name, r.type) " +
            "from User u " +
            "left join u.role r " +
            "where u.id = ?1")
    UserDto getUserDto(String userID);
}
