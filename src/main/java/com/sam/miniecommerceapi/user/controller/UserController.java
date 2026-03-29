package com.sam.miniecommerceapi.user.controller;

import com.sam.miniecommerceapi.shared.annotation.CurrentUserId;
import com.sam.miniecommerceapi.shared.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
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
    public ResponseEntity<SuccessApi<PageResponse<UserResponse>>> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        PageResponse<UserResponse> users = userService.getAllUsers(pageNumber, pageSize);

        return ApiFactory.success(users, "Get all users successfully.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<SuccessApi<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ApiFactory.success(response, "Get user successfully.");
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user")
    public ResponseEntity<SuccessApi<UserResponse>> me(@CurrentUserId Long id) {
        UserResponse response = userService.getUser(id);
        return ApiFactory.success(response, "Get me successfully");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user (soft delete)")
    public ResponseEntity<SuccessApi<String>> softDelete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiFactory.success("Delete user successfully");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user information", description = "Ignore fields if not update")
    public ResponseEntity<SuccessApi<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest r
    ) {
        UserResponse response = userService.updateUser(id, r);
        return ApiFactory.success(response, "Update information successfully");
    }

    @PostMapping("/{id}")
    @Operation(summary = "Restore user", description = "The user has been deleted (soft delete)")
    ResponseEntity<SuccessApi<String>> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return ApiFactory.success("Restore user successfully");
    }
}
