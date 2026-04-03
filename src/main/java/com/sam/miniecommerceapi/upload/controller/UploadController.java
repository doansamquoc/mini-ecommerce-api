package com.sam.miniecommerceapi.upload.controller;

import com.sam.miniecommerceapi.shared.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.upload.dto.response.SignatureResponse;
import com.sam.miniecommerceapi.upload.service.UploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadController {
    UploadService uploadService;

    @GetMapping("/signature")
    ResponseEntity<SuccessApi<SignatureResponse>> getSignature() {
        SignatureResponse response = uploadService.generateSignature();
        return ApiFactory.success(response, "Success");
    }
}
