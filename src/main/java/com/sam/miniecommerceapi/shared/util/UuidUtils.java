package com.sam.miniecommerceapi.shared.util;

import java.util.UUID;

public class UuidUtils {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
