package com.sam.miniecommerceapi.upload.service;

import com.sam.miniecommerceapi.upload.dto.response.SignatureResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadService {
    SignatureResponse generateSignature();

    Map upload(MultipartFile file);
}
