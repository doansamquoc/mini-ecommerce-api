package com.sam.miniecommerceapi.user.service;

import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;

import java.util.Optional;

public interface UserService {
    User oauth2Process(String email, String provider, String providerId);

    User findByIdentifier(String identifier);

    User findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);

    UserResponse updateUser(Long id, UserUpdateRequest r);

    PageResponse<UserResponse> getAllUsers(int pageNumber, int pageSize);

    UserResponse getUser(Long id);

    void deleteUser(Long id);

    void restoreUser(Long id);

    User getReference(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsById(Long id);

    User saveUser(User user);

    User findById(Long id);

    User createUser(UserCreationRequest request);
}
