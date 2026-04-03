package com.sam.miniecommerceapi.upload.service;

import com.sam.miniecommerceapi.upload.dto.response.SignatureResponse;

public interface UploadService {
    SignatureResponse generateSignature();
}
