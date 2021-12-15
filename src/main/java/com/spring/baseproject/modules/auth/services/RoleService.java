package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.base.models.PageDto;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.exceptions.ResponseException;
import com.spring.baseproject.modules.auth.models.dtos.NewRoleDto;
import com.spring.baseproject.modules.auth.models.dtos.RoleDto;
import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import com.spring.baseproject.modules.auth.repositories.RoleRepository;
import com.spring.baseproject.utils.jpa.JPAQueryBuilder;
import com.spring.baseproject.utils.jpa.JPAQueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JPAQueryExecutor jpaQueryExecutor;

    public PageDto<RoleDto> getRoles(RoleType queryRoleType,
                                     List<String> sortBy, List<String> sortType,
                                     int pageIndex, int pageSize) {
        JPAQueryBuilder<RoleDto> jpaQueryBuilder = new JPAQueryBuilder<>();
        jpaQueryBuilder.selectAsObject(RoleDto.class, "r.id", "r.name", "r.type")
                .from(Role.class, "r");
        if (queryRoleType != null) {
            jpaQueryBuilder.where(jpaQueryBuilder.newCondition().paramCondition("t.type", "=", queryRoleType));
        }
        jpaQueryBuilder.orderBy(sortBy, sortType);
        jpaQueryBuilder.setPagination(pageIndex, pageSize);
        return jpaQueryExecutor.executePaginationQuery(jpaQueryBuilder);
    }

    public RoleDto getRole(int roleID) throws ResponseException {
        RoleDto role = roleRepository.getRoleDto(roleID);
        if (role == null) {
            throw new ResponseException(ResponseValue.ROLE_NOT_FOUND);
        }
        return role;
    }

    public RoleDto createNewRole(NewRoleDto newRoleDto) throws ResponseException {
        try {
            Role role = new Role(newRoleDto);
            role = roleRepository.save(role);
            return new RoleDto(role);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseException(ResponseValue.ROLE_EXISTS);
        }
    }

    public RoleDto updateRole(int roleID, NewRoleDto newRoleDto) throws ResponseException {
        Role role = roleRepository.findFirstById(roleID);
        if (role == null) {
            throw new ResponseException(ResponseValue.ROLE_NOT_FOUND);
        }
        role.update(newRoleDto);
        role = roleRepository.save(role);
        return new RoleDto(role);
    }

    public void deleteRoles(Set<Integer> roleIDs) {
        roleRepository.deleteAllByIdIn(roleIDs);
    }
}
