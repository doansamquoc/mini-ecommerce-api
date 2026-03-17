package com.sam.miniecommerceapi.user.controller;

import com.sam.miniecommerceapi.common.dto.response.api.SuccessApi;
import com.sam.miniecommerceapi.common.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<SuccessApi<PageResponse<UserResponse>>> getAllUsers(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        PageResponse<UserResponse> users = userService.getAllUsers(pageNumber, pageSize);

        return ApiFactory.success(users, "Get all users successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessApi<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ApiFactory.success(response, "Get user successfully.");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessApi<String>> softDelete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiFactory.success("Delete successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessApi<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest r
    ) {
        UserResponse response = userService.updateUser(id, r);
        return ApiFactory.success(response, "Update information successfully.");
    }
}
