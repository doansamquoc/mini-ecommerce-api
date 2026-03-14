package com.sam.miniecommerceapi.user.service;

import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;

import java.util.Optional;

public interface UserService {
    User findByIdentifier(String identifier);

    User findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    UserResponse updateUser(String id, UserUpdateRequest r);

    PageResponse<UserResponse> getAllUsers(int pageNumber, int pageSize);

    UserResponse getUser(String id);

    void deleteUser(String id);

    void restoreUser(String id);

    User getReference(String id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    User saveUser(User user);

    User findById(String id);
}
