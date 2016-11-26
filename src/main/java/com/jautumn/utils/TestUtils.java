package com.jautumn.utils;

import org.apache.commons.lang3.ClassUtils;

public class TestUtils {

    public static String getFileNameWithPackage(String fileName, Class type) {
        return ClassUtils.getPackageName(type).replaceAll("\\.", "/") + "/" + fileName;
    }
}
