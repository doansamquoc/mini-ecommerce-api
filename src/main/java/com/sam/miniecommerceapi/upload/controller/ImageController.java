package com.sam.miniecommerceapi.upload.controller;

import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.common.util.SortUtils;
import com.sam.miniecommerceapi.upload.dto.request.ImageAdditionRequest;
import com.sam.miniecommerceapi.upload.dto.response.ImageResponse;
import com.sam.miniecommerceapi.upload.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Image Endpoints")
@RequestMapping("/api/v1/images")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
	ImageService imageService;

	@PostMapping
	@Operation(summary = "Create a new image.")
	ResponseEntity<ApiResponse<ImageResponse>> createImage(@Valid @RequestBody ImageAdditionRequest req) {
		ImageResponse response = imageService.createImage(req);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(response));
	}

	@GetMapping
	@Operation(summary = "Get all images with pagination.")
	ResponseEntity<PageResponse<ImageResponse>> getAll(
		@RequestParam(name = "page", required = false, defaultValue = "1") int pageNumber,
		@RequestParam(name = "sort", required = false, defaultValue = "createdAt,desc") String[] sorts
	) {
		Pageable pageable = PageRequest.of((pageNumber - 1), 10, Sort.by(SortUtils.normalizeSort(sorts)));
		PageResponse<ImageResponse> responses = imageService.getImages(pageable);
		return ResponseEntity.ok(responses);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an images by ID.")
	ResponseEntity<ApiResponse<?>> deleteById(@PathVariable Long id) {
		imageService.deleteImage(id);
		return ResponseEntity.ok(ApiResponse.of());
	}
}
