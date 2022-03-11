package com.spring.baseproject.utils.base;

import java.lang.reflect.Field;

public class ClassUtils {
    public static Field getNestedClassField(Class<?> clazz, String[] fieldPaths) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        Field result = null;
        for (String field : fieldPaths) {
            result = currentClass.getDeclaredField(field);
            currentClass = result.getType();
        }
        return result;
    }
}
