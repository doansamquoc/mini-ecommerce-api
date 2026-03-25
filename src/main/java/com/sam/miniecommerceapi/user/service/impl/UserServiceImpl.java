package com.sam.miniecommerceapi.user.service.impl;

import com.sam.miniecommerceapi.common.dto.response.pagination.PageResponse;
import com.sam.miniecommerceapi.common.enums.ErrorCode;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.common.util.UsernameUtils;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.mapper.UserMapper;
import com.sam.miniecommerceapi.user.repository.UserRepository;
import com.sam.miniecommerceapi.user.service.UserService;
import com.sam.miniecommerceapi.user.util.DisplayNameUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository repository;
    PasswordEncoder encoder;
    UserMapper mapper;

    /**
     * Find user with identifier (Username, email, phone)
     *
     * @param identifier Could be username, email or phone number
     * @return User
     */
    @Override
    public User findByIdentifier(String identifier) {
        return repository.findByIdentifier(identifier).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * Find user with email
     *
     * @param email User's email
     * @return User
     */
    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * @param email User's email
     * @return Optional user
     */
    @Override
    public Optional<User> findOptionalByEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Update user. Update by owner or admin
     *
     * @param id User ID
     * @param r  UserUpdateRequest
     * @return UserResponse
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public UserResponse updateUser(Long id, UserUpdateRequest r) {
        User user = getReference(id);
        user = mapper.toUser(r, user);
        return mapper.toResponse(repository.save(user));
    }

    /**
     * Get all users. Only admin can do this
     *
     * @param pageNumber Page number
     * @param pageSize   Page size
     * @return PageResponse<UserResponse>
     */
    @Override
    public PageResponse<UserResponse> getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repository.findAll(pageable);

        Page<UserResponse> userResponses = page.map(mapper::toResponse);

        return PageResponse.from(userResponses);
    }

    /**
     * @param id user ID
     * @return UserResponse
     */
    @Override
    public UserResponse getUser(Long id) {
        return mapper.toResponse(findById(id));
    }

    /**
     * Soft delete.
     * Mark user as deleted
     *
     * @param id User ID
     */
    @Override
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public void deleteUser(Long id) {
        User user = findById(id);
        repository.delete(user);
    }

    /**
     * Restore user data. Mark user as not deleted
     *
     * @param id User ID
     */
    @Override
    public void restoreUser(Long id) {
        repository.restoreById(id);
    }

    @Override
    public User getReference(Long id) {
        return repository.getReferenceById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return repository.existsByPhone(phone);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User createUser(UserCreationRequest request) {
        if (existsByEmail(request.getEmail())) throw new BusinessException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);

        String username = UsernameUtils.generateUsername(request.getEmail());
        String hashedPassword = encoder.encode(request.getPassword());
        String displayName = DisplayNameUtils.generateDisplayName(request.getEmail());

        User user = mapper.toUser(request);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setDisplayName(displayName);

        return repository.save(user);
    }
}
