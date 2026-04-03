package com.sam.miniecommerceapi.shared.util;

import java.util.UUID;

public class UUIDUtils {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateSlugSuffix() {
        return Long.toString(System.currentTimeMillis()).substring(0, 4) + ".html";
    }
}
