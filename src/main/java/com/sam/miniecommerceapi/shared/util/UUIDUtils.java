package com.sam.miniecommerceapi.shared.util;

import java.util.UUID;

public class UUIDUtils {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateSlugSuffix() {
        String nano = Long.toString(System.nanoTime());
        return nano.substring(nano.length() - 4) + ".html";
    }
}
