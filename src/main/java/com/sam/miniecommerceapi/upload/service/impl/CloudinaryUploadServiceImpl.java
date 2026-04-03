package com.sam.miniecommerceapi.upload.service.impl;

import com.cloudinary.Cloudinary;
import com.sam.miniecommerceapi.shared.config.AppProperties;
import com.sam.miniecommerceapi.upload.dto.response.SignatureResponse;
import com.sam.miniecommerceapi.upload.service.UploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryUploadServiceImpl implements UploadService {
    AppProperties properties;
    Cloudinary cloudinary;

    @Override
    public SignatureResponse generateSignature() {
        long timestamp = System.currentTimeMillis() / 1000;
        String folder = "ecommerce";
        String uploadPreset = "ecommerce_preset";

        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", folder);
        paramsToSign.put("upload_preset", uploadPreset);

        String signature = cloudinary.apiSignRequest(paramsToSign, properties.getCloudinaryApiSecret(), 1);

        return new SignatureResponse(
                timestamp,
                signature,
                properties.getCloudinaryName(),
                properties.getCloudinaryApiKey(),
                folder,
                uploadPreset
        );
    }
}
