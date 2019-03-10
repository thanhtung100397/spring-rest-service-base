package com.spring.baseproject.modules.demo.repositories;

import com.spring.baseproject.modules.demo.models.entities.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public List<User> getUsers() {
        List<User> result = new ArrayList<>(users.values());
        result.sort(Comparator.comparing(User::getUsername));
        return result;
    }

    public User getUserByUsername(String username) {
        return users.get(username);
    }

    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void deleteUser(String username) {
        users.remove(username);
    }

    public void deleteUserHasUsernameIn(Set<String> listUsername) {
        for (String username : listUsername) {
            users.remove(username);
        }
    }
}
