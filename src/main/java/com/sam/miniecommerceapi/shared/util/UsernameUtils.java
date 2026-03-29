package com.sam.miniecommerceapi.shared.util;

import java.util.Random;

public class UsernameUtils {
    public static String generateUsername(String email) {
        Random random = new Random();
        String prefix = email.substring(0, email.indexOf("@")).toLowerCase();
        int number = random.nextInt(9000) + 1000; // generate number from 1000 to 9999
        return prefix + "_" + number; // example: john_9382
    }
}
