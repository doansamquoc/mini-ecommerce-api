package com.sam.miniecommerceapi.common.util;

public class DisplayNameUtils {
    public static String generateDisplayName(String email) {
        return email.substring(0, email.indexOf('@')).toLowerCase();
    }
}
