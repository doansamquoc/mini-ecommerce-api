package com.sam.miniecommerceapi.auth.config.jwt;

import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthResponder {
  void sendError(HttpServletResponse response, ErrorCode errorCode, String path) throws IOException;
}
