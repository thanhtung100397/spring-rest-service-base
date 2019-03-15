package com.spring.baseproject.modules.auth.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.base.models.PageDto;
import com.spring.baseproject.constants.ResponseValue;
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

    public BaseResponse getRoles(RoleType queryRoleType,
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
        PageDto<RoleDto> queryResults = jpaQueryExecutor.executePaginationQuery(jpaQueryBuilder);
        return new BaseResponse<>(ResponseValue.SUCCESS, queryResults);
    }

    public BaseResponse getRole(int roleID) {
        RoleDto roleDto = roleRepository.getRoleDto(roleID);
        if (roleDto == null) {
            return new BaseResponse(ResponseValue.ROLE_NOT_FOUND);
        }
        return new BaseResponse<>(ResponseValue.SUCCESS, roleDto);
    }

    public BaseResponse createNewRole(NewRoleDto newRoleDto) {
        try {
            Role role = new Role(newRoleDto);
            role = roleRepository.save(role);
            return new BaseResponse<>(ResponseValue.SUCCESS, new RoleDto(role));
        } catch (DataIntegrityViolationException e) {
            return new BaseResponse(ResponseValue.ROLE_EXISTS);
        }
    }

    public BaseResponse updateRole(int roleID, NewRoleDto newRoleDto) {
        Role role = roleRepository.findFirstById(roleID);
        if (role == null) {
            return new BaseResponse(ResponseValue.ROLE_NOT_FOUND);
        }
        role.update(newRoleDto);
        role = roleRepository.save(role);
        return new BaseResponse<>(ResponseValue.SUCCESS, new RoleDto(role));
    }

    public BaseResponse deleteRoles(Set<Integer> roleIDs) {
        roleRepository.deleteAllByIdIn(roleIDs);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
