package com.sam.miniecommerceapi.notification.controller;

import com.sam.miniecommerceapi.notification.dto.request.BulkEmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.EmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.TemplateEmailRequest;
import com.sam.miniecommerceapi.notification.service.EmailService;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sends")
@Tag(name = "Send email endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendEmailController {
    EmailService emailService;

    @PostMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Send an email with attachment")
    public ResponseEntity<ApiResponse<String>> sendWithAttachment(
            @Valid @ModelAttribute EmailRequest request,
            @RequestPart(name = "file") MultipartFile file
    ) throws IOException {
        byte[] attachment = file.getBytes();
        String fileName = file.getOriginalFilename();

        emailService.sendWithAttachment(request, attachment, fileName);
        return ResponseEntity.accepted().body(ApiResponse.of());
    }

    @PostMapping("/template")
    @Operation(summary = "Send template email")
    public ResponseEntity<ApiResponse<String>> sendTemplate(@Valid @RequestBody TemplateEmailRequest request) {
        emailService.sendTemplateEmail(request);
        return ResponseEntity.accepted().body(ApiResponse.of());
    }

    @PostMapping("/bulk")
    @Operation(summary = "Send bulk emails")
    public ResponseEntity<ApiResponse<String>> sendBulk(@Valid @RequestBody BulkEmailRequest request) {
        emailService.sendBulkEmail(request);
        return ResponseEntity.accepted().body(ApiResponse.of());
    }

    @PostMapping("/html")
    @Operation(summary = "Send HTML email")
    public ResponseEntity<ApiResponse<String>> sendHTML(@Valid @RequestBody EmailRequest request) {
        emailService.sendHtmlEmail(request);
        return ResponseEntity.accepted().body(ApiResponse.of());
    }

    @PostMapping("/simple")
    @Operation(summary = "Send simple email")
    public ResponseEntity<ApiResponse<String>> sendSimple(@Valid @RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(request);
        return ResponseEntity.accepted().body(ApiResponse.of());
    }
}
