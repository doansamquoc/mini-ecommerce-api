package com.sam.miniecommerceapi.auth.mapper;

import com.sam.miniecommerceapi.auth.dto.internal.TokenDTO;

public interface AuthMapper {
    TokenDTO toLoginResult();
}
