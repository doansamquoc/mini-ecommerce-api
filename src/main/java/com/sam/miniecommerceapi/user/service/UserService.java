package com.sam.miniecommerceapi.user.service;

import com.sam.miniecommerceapi.user.entity.User;

public interface UserService {
    User findByIdentifier(String identifier);

    User getReference(String id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
