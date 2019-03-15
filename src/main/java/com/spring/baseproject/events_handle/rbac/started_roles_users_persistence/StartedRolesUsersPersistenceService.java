package com.spring.baseproject.events_handle.rbac.started_roles_users_persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.baseproject.annotations.event.EventHandler;
import com.spring.baseproject.events_handle.ApplicationEvent;
import com.spring.baseproject.events_handle.ApplicationEventHandle;
import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import com.spring.baseproject.modules.auth.models.entities.User;
import com.spring.baseproject.modules.auth.repositories.RoleRepository;
import com.spring.baseproject.modules.rbac.models.dtos.StartedRoleDto;
import com.spring.baseproject.modules.rbac.models.dtos.StartedRolesUsersDto;
import com.spring.baseproject.modules.rbac.models.dtos.StartedUserDto;
import com.spring.baseproject.modules.rbac.repositories.RBACRolesRepository;
import com.spring.baseproject.modules.rbac.repositories.RBACUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;


@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP, order = 1)
public class StartedRolesUsersPersistenceService implements ApplicationEventHandle {
    private static Logger logger = LoggerFactory.getLogger(StartedRolesUsersPersistenceService.class);

    @Value("${application.rbac.rbac-started-roles-users.path:rbac/rbac-started-roles-users.json}")
    private String startedRolesAndUsersFilePath;
    @Value("${application.rbac.refresh:false}")
    private boolean isActive;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private RBACRolesRepository RBACRolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RBACUserRepository RBACUserRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String startMessage() {
        return "Start persisting started roles and users...";
    }

    @Override
    public String successMessage() {
        if (isActive) {
            return "Started roles and users persistence...OK";
        } else {
            return "Started roles and users persistence...SKIPPED";
        }
    }

    @Override
    public void handleEvent() throws Exception {
        if (isActive) {
            InputStream inputStream = resourceLoader
                    .getResource("classpath:" + startedRolesAndUsersFilePath)
                    .getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            StartedRolesUsersDto startedRolesUsersDto = objectMapper.readValue(inputStream, StartedRolesUsersDto.class);

            Map<String, StartedRoleDto> startedRolesDto = startedRolesUsersDto.getStartedRoles();
            Set<Role> existedRoles = RBACRolesRepository.findAllByNameIn(startedRolesDto.keySet());
            Map<String, Role> roles = new HashMap<>();
            for (Role existRole : existedRoles) {
                roles.put(existRole.getName(), existRole);
                startedRolesDto.remove(existRole.getName());
                logger.info("Role [" + existRole.getName() + "] exists, skip persisting");
            }
            List<Role> newRoles = new ArrayList<>();
            for (Map.Entry<String, StartedRoleDto> entry : startedRolesDto.entrySet()) {
                StartedRoleDto startedRoleDto = entry.getValue();
                try {
                    RoleType type = RoleType.valueOf(startedRoleDto.getType());
                    Role role = new Role(entry.getKey(), type);
                    newRoles.add(role);
                    roles.put(entry.getKey(), role);
                } catch (IllegalArgumentException e) {
                    throw new Exception("Role [" + entry.getKey() +
                            "] has type [" + startedRoleDto.getType() + "] which is not declared in enum " +
                            RoleType.class.getName());
                }
            }
            roleRepository.saveAll(newRoles);

            Map<String, StartedUserDto> startedUsersDto = startedRolesUsersDto.getStartedUsers();
            Set<User> existUsers = RBACUserRepository.findAllByUsernameIn(startedUsersDto.keySet());
            for (User existUser : existUsers) {
                startedUsersDto.remove(existUser.getUsername());
                logger.info("User [" + existUser.getUsername() + "] exists, skip persisting");
            }
            Set<User> newUsers = new HashSet<>();
            for (Map.Entry<String, StartedUserDto> entry : startedUsersDto.entrySet()) {
                StartedUserDto startedUserDto = entry.getValue();
                User user = new User();
                user.setUsername(entry.getKey());
                user.setPassword(passwordEncoder.encode(startedUserDto.getPassword()));
                Role userRole = null;
                if (startedUserDto.getRoleName() != null) {
                    userRole = roles.get(startedUserDto.getRoleName());
                }
                user.setRole(userRole);
                newUsers.add(user);
            }
            if (newUsers.size() > 0) {
                RBACUserRepository.saveAll(newUsers);
            }
        }
    }
}
