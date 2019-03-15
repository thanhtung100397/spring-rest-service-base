package com.spring.baseproject.utils.auth;

import com.spring.baseproject.utils.base.ClassScannerUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class RouteScannerUtils {

    public interface RouteFetched {
        void onNewContainerClass(Class<?> containerClass);

        void onNewMethod(Method method);

        void onNewRouteFetched(RequestMethod method, String route);
    }

    public interface ApiFilter {
        boolean allowApi(Class<?> containClass, Method method);
    }

    public static void scanRoutes(String basePackage,
                                  Set<Class<? extends Annotation>> includedAnnotationClasses,
                                  Set<Class<? extends Annotation>> excludedAnnotationClasses,
                                  ApiFilter apiFilter,
                                  RouteFetched routeFetched,
                                  boolean toAntPattern) {
        if (includedAnnotationClasses == null) {
            includedAnnotationClasses = new HashSet<>();
        }
        includedAnnotationClasses.add(RestController.class);
        ClassScannerUtils.findClass(basePackage,
                includedAnnotationClasses,
                excludedAnnotationClasses,
                restControllerClass -> {
                    routeFetched.onNewContainerClass(restControllerClass);
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
                            routeFetched.onNewMethod(apiMethod);
                            fetchApis(apiMethod, parentPaths, routeFetched, toAntPattern);
                        }
                    }
                    return true;
                });
    }

    private static void fetchApis(Method method, String[] parentPaths,
                                  RouteFetched routeFetched, boolean toAntPattern) {
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
                                        patchMapping.value(), patchMapping.path(), routeFetched, toAntPattern);
                            }
                        } else {
                            newApi(parentPaths, new RequestMethod[]{RequestMethod.DELETE},
                                    deleteMapping.value(), deleteMapping.path(), routeFetched, toAntPattern);
                        }
                    } else {
                        newApi(parentPaths, new RequestMethod[]{RequestMethod.PUT},
                                putMapping.value(), putMapping.path(), routeFetched, toAntPattern);
                    }
                } else {
                    newApi(parentPaths, new RequestMethod[]{RequestMethod.POST},
                            postMapping.value(), postMapping.path(), routeFetched, toAntPattern);
                }
            } else {
                newApi(parentPaths, new RequestMethod[]{RequestMethod.GET},
                        getMapping.value(), getMapping.path(), routeFetched, toAntPattern);
            }
        } else {
            newApi(parentPaths, requestMapping.method(),
                    requestMapping.value(), requestMapping.path(), routeFetched, toAntPattern);
        }
    }

    private static void newApi(String[] parentPaths,
                               RequestMethod[] methods, String[] values, String[] paths,
                               RouteFetched routeFetched, boolean toAntPattern) {
        String[] targetPaths = null;
        if (values.length > 0) {
            targetPaths = values;
        } else if (paths.length > 0) {
            targetPaths = paths;
        } else {
            targetPaths = new String[parentPaths.length];
        }
        if (targetPaths.length > 0) {
            for (String path : targetPaths) {
                path = toStandardPath(path);
                for (RequestMethod method : methods) {
                    if (parentPaths.length > 0) {
                        for (String parentPath : parentPaths) {
                            path = parentPath + path;
                            if (toAntPattern) {
                                path = toAntPattern(path);
                            }
                            routeFetched.onNewRouteFetched(method, path);
                        }
                    } else {
                        if (toAntPattern) {
                            path = toAntPattern(path);
                        }
                        routeFetched.onNewRouteFetched(method, path);
                    }
                }
            }
        }
    }

    public static String toStandardPath(String path) {
        if (path == null) {
            return "";
        }
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
