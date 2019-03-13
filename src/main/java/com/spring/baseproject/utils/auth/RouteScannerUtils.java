package com.spring.baseproject.utils.auth;

import com.spring.baseproject.utils.base.ClassScannerUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class RouteScannerUtils {
    public interface NewRouteFetched {
        void onNewRouteFetched(RequestMethod method, String route);
    }

    public interface ApiFilter {
        boolean allowApi(Class<?> containClass, Method method);
    }

    public static void scanRoutes(String basePackage,
                                  Set<Class<? extends Annotation>> includedAnnotationClasses,
                                  Set<Class<? extends Annotation>> excludedAnnotationClasses,
                                  ApiFilter apiFilter,
                                  NewRouteFetched newRouteFetched,
                                  boolean toAntPattern) {
        if (includedAnnotationClasses == null) {
            includedAnnotationClasses = new HashSet<>();
        }
        includedAnnotationClasses.add(RestController.class);
        ClassScannerUtils.findClass(basePackage,
                includedAnnotationClasses,
                excludedAnnotationClasses,
                restControllerClass -> {
                    String[] parentPaths;
                    RequestMapping controllerRequestMapping = restControllerClass.getDeclaredAnnotation(RequestMapping.class);
                    if (controllerRequestMapping != null) {
                        parentPaths = controllerRequestMapping.value();
                        for (int i = 0; i < parentPaths.length; i++) {
                            StringBuilder parentPath = new StringBuilder(parentPaths[i]);
                            if (parentPath.charAt(parentPath.length() - 1) == '/') {
                                parentPath.deleteCharAt(parentPath.length() - 1);
                            }
                            if (parentPath.charAt(0) != '/') {
                                parentPath.insert(0, "/");
                            }
                            parentPaths[i] = parentPath.toString();
                        }
                    } else {
                        parentPaths = new String[0];
                    }
                    Method[] apiMethods = restControllerClass.getDeclaredMethods();
                    for (Method apiMethod : apiMethods) {
                        if (apiFilter == null || apiFilter.allowApi(restControllerClass, apiMethod)) {
                            fetchApis(apiMethod, parentPaths, newRouteFetched, toAntPattern);
                        }
                    }
                    return true;
                });
    }

    private static void fetchApis(Method method, String[] parentPaths,
                                  NewRouteFetched newRouteFetched, boolean toAntPattern) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            GetMapping getMapping = method.getDeclaredAnnotation(GetMapping.class);
            if (getMapping == null) {
                PostMapping postMapping = method.getDeclaredAnnotation(PostMapping.class);
                if (postMapping == null) {
                    PutMapping putMapping = method.getDeclaredAnnotation(PutMapping.class);
                    if (putMapping == null) {
                        DeleteMapping deleteMapping = method.getDeclaredAnnotation(DeleteMapping.class);
                        if (deleteMapping == null) {
                            PatchMapping patchMapping = method.getDeclaredAnnotation(PatchMapping.class);
                            if (patchMapping != null) {
                                newApi(parentPaths, new RequestMethod[]{RequestMethod.PATCH},
                                        patchMapping.value(), patchMapping.path(), newRouteFetched, toAntPattern);
                            }
                        } else {
                            newApi(parentPaths, new RequestMethod[]{RequestMethod.DELETE},
                                    deleteMapping.value(), deleteMapping.path(), newRouteFetched, toAntPattern);
                        }
                    } else {
                        newApi(parentPaths, new RequestMethod[]{RequestMethod.PUT},
                                putMapping.value(), putMapping.path(), newRouteFetched, toAntPattern);
                    }
                } else {
                    newApi(parentPaths, new RequestMethod[]{RequestMethod.POST},
                            postMapping.value(), postMapping.path(), newRouteFetched, toAntPattern);
                }
            } else {
                newApi(parentPaths, new RequestMethod[]{RequestMethod.GET},
                        getMapping.value(), getMapping.path(), newRouteFetched, toAntPattern);
            }
        } else {
            newApi(parentPaths, requestMapping.method(),
                    requestMapping.value(), requestMapping.path(), newRouteFetched, toAntPattern);
        }
    }

    private static void newApi(String[] parentPaths,
                               RequestMethod[] methods, String[] values, String[] paths,
                               NewRouteFetched newRouteFetched, boolean toAntPattern) {
        Set<String> routes = new HashSet<>();
        if (values.length > 0) {
            for (String value : values) {
                routes.add(toStandardPath(value));
            }
        } else if (paths.length > 0) {
            for (String path : paths) {
                routes.add(toStandardPath(path));
            }
        }
        if (parentPaths.length > 0) {
            for (String parentPath : parentPaths) {
                for (String route : routes) {
                    for (RequestMethod method : methods) {
                        String path = parentPath + route;
                        if (toAntPattern) {
                            path = toAntPattern(path);
                        }
                        newRouteFetched.onNewRouteFetched(method, path);
                    }
                }
            }
        } else {
            for (String route : routes) {
                for (RequestMethod method : methods) {
                    if (toAntPattern) {
                        route = toAntPattern(route);
                    }
                    newRouteFetched.onNewRouteFetched(method, route);
                }
            }
        }
    }

    public static String toStandardPath(String path) {
        StringBuilder result = new StringBuilder(path);
        if (result.charAt(0) != '/') {
            result.insert(0, "/");
        }
        return result.toString();
    }

    public static String toAntPattern(String path) {
        return path.replaceAll("\\{\\w+\\}", "*");
    }
}
