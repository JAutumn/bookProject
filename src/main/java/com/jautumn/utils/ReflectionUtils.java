package com.jautumn.utils;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static String getStringFromPrivateStaticField(Class type, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field privateStaticField = type.getDeclaredField(fieldName);
        privateStaticField.setAccessible(true);
        return (String) privateStaticField.get(null);
    }
}
