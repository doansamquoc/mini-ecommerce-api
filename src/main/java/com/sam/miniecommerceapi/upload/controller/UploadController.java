package com.sam.miniecommerceapi.upload.controller;

import com.sam.miniecommerceapi.shared.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.upload.dto.response.SignatureResponse;
import com.sam.miniecommerceapi.upload.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Upload Endpoints", description = "Upload images into Cloud service")
public class UploadController {
    UploadService uploadService;

    @GetMapping("/signature")
    @Operation(summary = "Get signature from Cloud", description = "The signature allowed to upload from client")
    ResponseEntity<SuccessApi<SignatureResponse>> getSignature() {
        SignatureResponse response = uploadService.generateSignature();
        return ApiFactory.success(response, "Success");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<SuccessApi<Map>> uploadImage(@RequestParam("image") MultipartFile file) {
        Map data = uploadService.upload(file);
        return ApiFactory.success(data, "Upload successful");
    }

}
