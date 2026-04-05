package com.sam.miniecommerceapi.notification.controller;

import com.sam.miniecommerceapi.notification.dto.request.EmailRequest;
import com.sam.miniecommerceapi.notification.service.EmailService;
import com.sam.miniecommerceapi.shared.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/send")
@Tag(name = "Send email endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendEmailController {
    EmailService emailService;

    @PostMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Send an email with attachment")
    ResponseEntity<ApiResponse<String>> sendWithAttachment(
            @Valid @RequestBody EmailRequest request,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        emailService.sendWithAttachment(request, file.getBytes(), file.getName());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of());
    }
}
