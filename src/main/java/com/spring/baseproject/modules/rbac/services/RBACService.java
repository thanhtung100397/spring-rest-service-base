package com.spring.baseproject.modules.rbac.services;

import com.spring.baseproject.base.models.BaseResponse;
import com.spring.baseproject.constants.ResponseValue;
import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import com.spring.baseproject.modules.rbac.models.dtos.ApiDto;
import com.spring.baseproject.modules.rbac.models.dtos.ApiFunctionDto;
import com.spring.baseproject.modules.rbac.models.dtos.ApiModuleDto;
import com.spring.baseproject.modules.rbac.models.entities.Api;
import com.spring.baseproject.modules.rbac.models.entities.ApiFunction;
import com.spring.baseproject.modules.rbac.models.entities.ApiMethod;
import com.spring.baseproject.modules.rbac.models.entities.ApiModule;
import com.spring.baseproject.modules.rbac.repositories.ApiRepository;
import com.spring.baseproject.modules.rbac.repositories.RBACRolesRepository;
import com.spring.baseproject.utils.jpa.SortAndPageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RBACService {
    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private RBACRolesRepository RBACRoleRepository;

    public BaseResponse getApis() {
        return getApiStructure(sort -> apiRepository.getAllApiWithFetchedFunctionAndModule(sort));
    }

    public BaseResponse getApisOfRole(int roleID) {
        Role role = RBACRoleRepository.findFirstById(roleID);
        if (role == null) {
            return new BaseResponse(ResponseValue.ROLE_NOT_FOUND);
        }
        return getApiStructure(sort -> {
            if (role.getType() == RoleType.ROOT) {
                return apiRepository.getAllApiWithFetchedFunctionAndModule(sort);
            } else {
                return apiRepository.getAllApiWithFetchedFunctionAndModuleOfRole(role.getId(), sort);
            }
        });
    }

    private interface QueryApis {
        List<Api> queryApis(Sort sort);
    }

    private BaseResponse getApiStructure(QueryApis queryApis) {
        List<String> sortBy = new ArrayList<>();
        List<String> sortType = new ArrayList<>();

        sortBy.add(Api.API_FUNCTION + "." + ApiFunction.API_MODULE + "." + ApiModule.PRIORITY);
        sortType.add("asc");

        sortBy.add(Api.API_FUNCTION + "." + ApiFunction.DESCRIPTION);
        sortType.add("asc");

        sortBy.add(Api.METHOD + "." + ApiMethod.PRIORITY);
        sortType.add("asc");

        Sort sort = SortAndPageFactory.createSort(sortBy, sortType);

        List<Api> apis = queryApis.queryApis(sort);

        List<ApiModuleDto> apiModulesDto = new ArrayList<>();
        ApiModuleDto currentApiModuleDto = new ApiModuleDto();
        ApiFunctionDto currentApiFunctionDto = new ApiFunctionDto();
        for (Api api : apis) {
            ApiFunction apiFunction = api.getApiFunction();
            ApiModule apiModule = apiFunction.getApiModule();
            if (!apiModule.getName().equals(currentApiModuleDto.getName())) {
                currentApiModuleDto = new ApiModuleDto(apiModule);
                apiModulesDto.add(currentApiModuleDto);
            }
            if (!apiFunction.getName().equals(currentApiFunctionDto.getName())) {
                currentApiFunctionDto = new ApiFunctionDto(apiFunction);
                currentApiModuleDto.addApiFunction(currentApiFunctionDto);
            }
            currentApiFunctionDto.addApi(new ApiDto(api));
        }

        return new BaseResponse<>(ResponseValue.SUCCESS, apiModulesDto);
    }

    public BaseResponse updateApisForRole(int roleID, Set<Integer> apiIDs) {
        Role role = RBACRoleRepository.getRoleWithFetchedApiWithFetchedRoles(roleID);
        if (role == null) {
            return new BaseResponse(ResponseValue.ROLE_NOT_FOUND);
        }
        if (role.getType() == RoleType.ROOT) {
            return new BaseResponse(ResponseValue.CANNOT_MODIFY_ROOT_ACCESS_GRANT);
        }
        Set<Api> roleApis = role.getApis();
        Set<Api> newApis;
        if (apiIDs == null || apiIDs.size() == 0) {
            newApis = new HashSet<>();
        } else {
            newApis = apiRepository.getAllApiWithFetchedRoles(apiIDs);
        }
        Set<Api> updatedApis = new HashSet<>();
        for (Api api : roleApis) {
            if (newApis.contains(api)) {
                newApis.remove(api);
            } else {
                api.getRoles().remove(role);
                updatedApis.add(api);
            }
        }
        for (Api newApi : newApis) {
            newApi.getRoles().add(role);
            updatedApis.add(newApi);
        }
        apiRepository.saveAll(updatedApis);
        return new BaseResponse(ResponseValue.SUCCESS);
    }
}
