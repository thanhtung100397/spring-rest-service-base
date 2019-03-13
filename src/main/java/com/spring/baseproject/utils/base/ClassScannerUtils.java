package com.spring.baseproject.utils.base;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.*;

public class ClassScannerUtils {
    public static void findClass(String basePackage,
                                 Set<Class<? extends Annotation>> includedAnnotationClasses,
                                 Set<Class<? extends Annotation>> excludedAnnotationClasses,
                                 ScannedClassProcessor processor) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        if (includedAnnotationClasses != null) {
            for (Class<? extends Annotation> includedAnnotationClass : includedAnnotationClasses) {
                scanner.addIncludeFilter(new AnnotationTypeFilter(includedAnnotationClass));
            }
        }
        if (excludedAnnotationClasses != null) {
            for (Class<? extends Annotation> excludeAnnotationClass : excludedAnnotationClasses) {
                scanner.addExcludeFilter(new AnnotationTypeFilter(excludeAnnotationClass));
            }
        }
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class<?> clazz;
            try {
                clazz = Class.forName(beanDefinition.getBeanClassName());
                boolean isContinuedIterating = processor.processScannedClass(clazz);
                if (!isContinuedIterating) {
                    break;
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
    }

    public static List<Class<?>> scanAnnotatedClasses(String basePackage,
                                                      Set<Class<? extends Annotation>> includedAnnotationClasses,
                                                      Set<Class<? extends Annotation>> excludedAnnotationClasses,
                                                      Filter filter, Comparator<Class<?>> order) {
        List<Class<?>> result = new ArrayList<>();
        findClass(basePackage, includedAnnotationClasses, excludedAnnotationClasses,
                clazz -> {
                    if (filter == null || filter.doFilter(clazz)) {
                        result.add(clazz);
                    }
                    return true;
                });
        if (order != null) {
            result.sort(order);
        }
        return result;
    }

    public static Class<?> findAnnotatedClass(String basePackage,
                                              Set<Class<? extends Annotation>> includedAnnotationClasses,
                                              Set<Class<? extends Annotation>> excludedAnnotationClasses,
                                              Filter filter) {
        Class<?> results[] = new Class[1];
        results[0] = null;
        findClass(basePackage, includedAnnotationClasses, excludedAnnotationClasses,
                clazz -> {
                    if (filter != null && filter.doFilter(clazz)) {
                        results[0] = clazz;
                        return false;
                    }
                    return true;
                });
        return results[0];
    }

    public interface ScannedClassProcessor {
        boolean processScannedClass(Class<?> clazz);
    }

    public interface Filter {
        boolean doFilter(Class<?> clazz);
    }
}
