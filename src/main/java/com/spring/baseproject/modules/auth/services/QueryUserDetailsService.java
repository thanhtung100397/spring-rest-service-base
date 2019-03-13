package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.modules.auth.models.dtos.CustomUserDetail;
import com.spring.baseproject.modules.auth.models.entities.User;
import com.spring.baseproject.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QueryUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> result = new ArrayList<>(1);
        if (user.getRole() != null) {
            result.add(new SimpleGrantedAuthority("" + user.getRole().getId()));
        }
        return result;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserFetchRole(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetail(user.getId(), user.getUsername(), user.getPassword(),
                getGrantedAuthorities(user));
    }
}
