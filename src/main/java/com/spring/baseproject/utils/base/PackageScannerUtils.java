package com.spring.baseproject.utils.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageScannerUtils {
    public static List<String> listAllSubPackages(String parentPackage) {
        File file = new File("src/main/java/" + parentPackage.replace(".", "/"));
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("parent is not package");
        }
        List<String> subPackages = new ArrayList<>();
        File[] listChildren = file.listFiles();
        if (listChildren != null) {
            for (File child : listChildren) {
                if (child.isDirectory()) {
                    subPackages.add(child.getName());
                }
            }
        }
        return subPackages;
    }
}
