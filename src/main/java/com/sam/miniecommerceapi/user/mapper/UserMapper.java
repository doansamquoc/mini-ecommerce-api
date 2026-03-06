package com.sam.miniecommerceapi.user.mapper;

import com.sam.miniecommerceapi.auth.dto.request.CreationRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(CreationRequest r);
    UserResponse toResponse(User user);
}
