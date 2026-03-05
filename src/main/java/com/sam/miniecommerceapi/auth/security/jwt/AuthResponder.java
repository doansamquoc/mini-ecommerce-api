package com.sam.miniecommerceapi.auth.security.jwt;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthResponder {
  void sendError(HttpServletResponse response, ErrorCode errorCode, String path) throws IOException;
}
