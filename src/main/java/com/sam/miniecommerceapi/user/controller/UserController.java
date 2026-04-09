package com.sam.miniecommerceapi.user.controller;

import com.sam.miniecommerceapi.common.annotation.CurrentUserId;
import com.sam.miniecommerceapi.common.dto.response.ApiResponse;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User endpoints")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
	UserService userService;

	@GetMapping()
	@Operation(summary = "Get all users")
	public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
		@RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
		@RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
	) {
		PageResponse<UserResponse> responses = userService.getAllUsers(pageNumber, pageSize);

		return ResponseEntity.ok(ApiResponse.of(responses));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get user by ID")
	public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
		UserResponse response = userService.getUser(id);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@GetMapping("/me")
	@Operation(summary = "Get current user")
	public ResponseEntity<ApiResponse<UserResponse>> me(@CurrentUserId Long id) {
		UserResponse response = userService.getUser(id);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete user (soft delete)")
	public ResponseEntity<ApiResponse<String>> softDelete(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(ApiResponse.of());
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update user information", description = "Ignore fields if not update")
	public ResponseEntity<ApiResponse<UserResponse>> updateUser(
		@PathVariable Long id,
		@Valid @RequestBody UserUpdateRequest r
	) {
		UserResponse response = userService.updateUser(id, r);
		return ResponseEntity.ok(ApiResponse.of(response));
	}

	@PostMapping("/{id}")
	@Operation(summary = "Restore user", description = "The user has been deleted (soft delete)")
	ResponseEntity<ApiResponse<String>> restoreUser(@PathVariable Long id) {
		userService.restoreUser(id);
		return ResponseEntity.ok(ApiResponse.of());
	}
}
