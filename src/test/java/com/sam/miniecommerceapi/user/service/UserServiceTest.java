package com.sam.miniecommerceapi.user.service;

import com.sam.miniecommerceapi.common.enums.Role;
import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import com.sam.miniecommerceapi.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceTest {
    @Mock
    UserRepository repository;

    @InjectMocks
    UserService userService;

    @Test
    void createUser_ShouldReturnResponse_WhenDataIsValid() {
        UserCreationRequest request = new UserCreationRequest("admin@miniecommerce.com", "123456");
        User userSaved = new User("admin", request.getEmail(), "0877242416", request.getPassword(), "Admin", Set.of(Role.ADMIN), false);

        when(repository.save(any(User.class))).thenReturn(userSaved);

//        UserResponse response = userService.();
    }
}