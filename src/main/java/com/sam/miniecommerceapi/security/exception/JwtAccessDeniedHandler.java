package com.sam.miniecommerceapi.security.exception;

import com.sam.miniecommerceapi.common.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
  AuthResponder responder;

  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AccessDeniedException accessDeniedException)
      throws IOException {
    responder.sendError(response, ErrorCode.ACCESS_DENIED, request.getServletPath());
  }
}
