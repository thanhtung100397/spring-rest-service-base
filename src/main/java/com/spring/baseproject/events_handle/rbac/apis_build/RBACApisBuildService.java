package com.spring.baseproject.events_handle.rbac.apis_build;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.baseproject.annotations.event.EventHandler;
import com.spring.baseproject.annotations.rbac.RoleBaseAccessControl;
import com.spring.baseproject.components.rbac.InMemoryRoutesDictionary;
import com.spring.baseproject.constants.ApplicationConstants;
import com.spring.baseproject.events_handle.ApplicationEvent;
import com.spring.baseproject.events_handle.ApplicationEventHandle;
import com.spring.baseproject.modules.auth.models.entities.Role;
import com.spring.baseproject.modules.auth.models.entities.RoleType;
import com.spring.baseproject.modules.auth.repositories.RoleRepository;
import com.spring.baseproject.modules.rbac.models.dtos.ModuleDescriptionDto;
import com.spring.baseproject.modules.rbac.models.entities.Api;
import com.spring.baseproject.modules.rbac.models.entities.ApiFunction;
import com.spring.baseproject.modules.rbac.models.entities.ApiMethod;
import com.spring.baseproject.modules.rbac.models.entities.ApiModule;
import com.spring.baseproject.modules.rbac.repositories.ApiFunctionRepository;
import com.spring.baseproject.modules.rbac.repositories.ApiMethodRepository;
import com.spring.baseproject.modules.rbac.repositories.ApiModuleRepository;
import com.spring.baseproject.modules.rbac.repositories.ApiRepository;
import com.spring.baseproject.utils.auth.RouteScannerUtils;
import com.spring.baseproject.utils.base.PackageScannerUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

@Service
@EventHandler(event = ApplicationEvent.ON_APPLICATION_STARTED_UP, order = 3)
public class RBACApisBuildService implements ApplicationEventHandle {
    private static Logger logger = LoggerFactory.getLogger(RBACApisBuildService.class);

    @Value("${application.rbac.rbac-modules-description.path:rbac/rbac-modules-description.json}")
    private String rbacModulesFilePath;
    @Value("${application.modules-package.name:modules}")
    private String rootModulePackageName;
    @Value("${application.rbac.refresh:false}")
    private boolean isActive;

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private ApiMethodRepository apiMethodRepository;
    @Autowired
    private ApiFunctionRepository apiFunctionRepository;
    @Autowired
    private ApiModuleRepository apiModuleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InMemoryRoutesDictionary inMemoryRoutesDictionary;

    @Override
    public String startMessage() {
        return "Start persisting apis...";
    }

    @Override
    public String successMessage() {
        if (isActive) {
            return "Apis persistence...OK";
        } else {
            return "Apis persistence...SKIPPED";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleEvent() throws Exception {
        String rootModulePackage = ApplicationConstants.BASE_PACKAGE_NAME + "." + rootModulePackageName;
        List<String> moduleNames = PackageScannerUtils.listAllSubPackages(rootModulePackage);

        List<Api> apis = null;
        Map<RoleType, Set<Role>> mapRoles = null;
        Map<String, Api> apiMap = null;
        Map<String, ApiMethod> apiMethodMap = null;
        Map<String, ApiFunction> apiFunctionMap = null;
        Map<String, ApiModule> apiModuleMap = null;
        if (isActive) {
            InputStream inputStream = resourceLoader
                    .getResource("classpath:" + rbacModulesFilePath)
                    .getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, ModuleDescriptionDto> moduleDescriptionsDto = objectMapper
                    .readValue(inputStream, new TypeReference<Map<String, ModuleDescriptionDto>>() {
                    });

            List<Role> listRoles = roleRepository.findAll();
            mapRoles = new HashMap<>();
            for (Role role : listRoles) {
                if (role.getType() != RoleType.ROOT) {
                    Set<Role> roles = mapRoles.computeIfAbsent(role.getType(), k -> new HashSet<>());
                    roles.add(role);
                }
            }

            apis = apiRepository.getAllFetchedApis();
            apiMap = new HashMap<>(apis.size());
            for (Api api : apis) {
                ApiMethod apiMethod = api.getMethod();
                String method = apiMethod == null ? "" : apiMethod.getName() + " ";
                apiMap.put(method + api.getRoute(), api);
            }

            List<ApiMethod> apiMethods = apiMethodRepository.findAll();
            apiMethodMap = new HashMap<>(apiMethods.size());
            for (ApiMethod apiMethod : apiMethods) {
                apiMethodMap.put(apiMethod.getName(), apiMethod);
            }

            List<ApiFunction> apiFunctions = apiFunctionRepository.findAll();
            apiFunctionMap = new HashMap<>(apiFunctions.size());
            for (ApiFunction apiFunction : apiFunctions) {
                apiFunctionMap.put(apiFunction.getName(), apiFunction);
            }

            List<ApiModule> apiModules = apiModuleRepository.findAll();
            apiModuleMap = new HashMap<>(moduleNames.size());
            for (ApiModule apiModule : apiModules) {
                apiModuleMap.put(apiModule.getName(), apiModule);
                ModuleDescriptionDto moduleDescriptionDto = moduleDescriptionsDto.get(apiModule.getName());
                if (moduleDescriptionDto != null) {
                    apiModule.update(moduleDescriptionDto);
                    apiModuleMap.put(apiModule.getName(), apiModule);
                    moduleDescriptionsDto.remove(apiModule.getName());
                }
            }
            for (String moduleName : moduleNames) {
                ApiModule existApiModule = apiModuleMap.get(moduleName);
                if (existApiModule == null) {
                    ApiModule apiModule = new ApiModule();
                    apiModule.setName(moduleName);
                    ModuleDescriptionDto moduleDescriptionDto = moduleDescriptionsDto.get(apiModule.getName());
                    if (moduleDescriptionDto != null) {
                        apiModule.update(moduleDescriptionDto);
                        moduleDescriptionsDto.remove(apiModule.getName());
                    }
                    apiModuleMap.put(apiModule.getName(), apiModule);
                }
            }
        }

        List<Api> savedApis = new ArrayList<>();
        int totalNewApi = 0;
        for (String moduleName : moduleNames) {
            ApiModule apiModule = null;
            if (apiModuleMap != null) {
                apiModule = apiModuleMap.get(moduleName);
            }
            int[] logs;
            if (isActive) {
                logs = new int[]{0, 0};
            } else {
                logs = null;
            }
            Map<String, ApiFunction> finalApiFunctionMap = apiFunctionMap;
            ApiModule finalApiModule = apiModule;
            Map<RoleType, Set<Role>> finalMapRoles = mapRoles;
            Map<String, Api> finalApiMap = apiMap;
            List<Api> finalApis = apis;
            Map<String, ApiMethod> finalApiMethodMap = apiMethodMap;
            RouteScannerUtils.scanRoutes(rootModulePackage + "." + moduleName + ".controllers",
                    null, null,
                    (containClass, method) -> containClass.getDeclaredAnnotation(RoleBaseAccessControl.class) != null ||
                            method.getDeclaredAnnotation(RoleBaseAccessControl.class) != null,
                    new RouteScannerUtils.RouteFetched() {
                        RoleBaseAccessControl parentRoleBaseAccessControl;
                        ApiFunction apiFunction;
                        String apiDescription;
                        Set<Role> accessibleRoles = new HashSet<>();

                        @Override
                        public void onNewContainerClass(Class<?> containerClass) {
                            if (finalApiFunctionMap != null) {
                                apiFunction = finalApiFunctionMap.get(containerClass.getSimpleName());
                                parentRoleBaseAccessControl = containerClass.getDeclaredAnnotation(RoleBaseAccessControl.class);
                                if (apiFunction == null) {
                                    apiFunction = new ApiFunction();
                                    apiFunction.setName(containerClass.getSimpleName());
                                    apiFunction.setApiModule(finalApiModule);
                                    io.swagger.annotations.Api apiAnnotation = containerClass.getDeclaredAnnotation(io.swagger.annotations.Api.class);
                                    if (apiAnnotation != null) {
                                        apiFunction.setDescription(apiAnnotation.description());
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNewMethod(Method method) {
                            if (finalMapRoles != null) {
                                ApiOperation apiOperation = method.getDeclaredAnnotation(ApiOperation.class);
                                if (apiOperation != null) {
                                    apiDescription = apiOperation.value();
                                } else {
                                    apiDescription = null;
                                }
                                RoleBaseAccessControl roleBaseAccessControl = method.getDeclaredAnnotation(RoleBaseAccessControl.class);
                                if (roleBaseAccessControl == null) {
                                    roleBaseAccessControl = parentRoleBaseAccessControl;
                                }
                                RoleType[] accessibleRoleTypes = roleBaseAccessControl.defaultAccess();
                                accessibleRoles = new HashSet<>();
                                accessibleRoles.clear();
                                for (RoleType roleType : accessibleRoleTypes) {
                                    Set<Role> roles = finalMapRoles.get(roleType);
                                    if (roles != null) {
                                        accessibleRoles.addAll(roles);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNewRouteFetched(RequestMethod method, String route) {
                            String fullPath = method.name() + " " + route;
                            inMemoryRoutesDictionary.addRoute(route);
                            if (finalApiMap != null) {
                                Api api = finalApiMap.get(fullPath);
                                if (api == null) {
                                    api = new Api(finalApiMethodMap.get(method.name()), route);
                                    finalApis.add(api);
                                    if (logs != null) {
                                        logs[0]++;
                                    }
                                } else {
                                    finalApiMap.remove(fullPath);
                                }
                                api.setApiFunction(apiFunction);
                                api.setDescription(apiDescription);
                                api.setRoles(accessibleRoles);
                                savedApis.add(api);
                                if (logs != null) {
                                    logs[1]++;
                                }
                            }
                        }
                    }, true);
            if (logs != null) {
                totalNewApi += logs[0];
                logger.info("Module [" + moduleName + "] scanned, found " + logs[0] + " new api(s), total " + logs[1] + " api(s)");
            }
        }

        if (apiMap != null) {
            Set<Api> removedApis = new HashSet<>();
            for (Map.Entry<String, Api> entrySet : apiMap.entrySet()) {
                removedApis.add(entrySet.getValue());
            }

            apiRepository.deleteAll(removedApis);
            apiRepository.saveAll(savedApis);
            apiFunctionRepository.deleteOrphanApiFunctions();
            apiModuleRepository.deleteOrphanApiModules();
            logger.info("Apis persistence result, added " + totalNewApi + " new api(s), removed " + removedApis.size() + " api(s), total " + savedApis.size() + " api(s)");
        }
    }
}
