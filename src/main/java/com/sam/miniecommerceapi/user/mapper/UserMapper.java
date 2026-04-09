package com.sam.miniecommerceapi.user.mapper;

import com.sam.miniecommerceapi.user.dto.request.UserCreationRequest;
import com.sam.miniecommerceapi.user.dto.request.UserUpdateRequest;
import com.sam.miniecommerceapi.user.dto.response.UserResponse;
import com.sam.miniecommerceapi.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

	User toUser(UserCreationRequest r);

	User toUser(UserUpdateRequest r, @MappingTarget User user);

	UserResponse toResponse(User user);
}
