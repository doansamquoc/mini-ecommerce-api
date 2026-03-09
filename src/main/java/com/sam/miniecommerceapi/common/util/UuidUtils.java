package com.sam.miniecommerceapi.common.util;

import java.util.UUID;

public class UuidUtils {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
