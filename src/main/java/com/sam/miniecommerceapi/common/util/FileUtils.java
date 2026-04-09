package com.sam.miniecommerceapi.common.util;

import org.springframework.http.MediaType;

public class FileUtils {
    public static String getContentType(String fileName) {
        if (fileName.endsWith(".pdf")) return MediaType.APPLICATION_PDF_VALUE;
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        if (fileName.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
