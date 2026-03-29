package com.sam.miniecommerceapi.auth.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.miniecommerceapi.shared.dto.response.api.ErrorApi;
import com.sam.miniecommerceapi.shared.dto.response.api.factory.ApiFactory;
import com.sam.miniecommerceapi.shared.constant.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAuthResponder implements AuthResponder {
    ObjectMapper mapper;

    @Override
    public void sendError(HttpServletResponse response, ErrorCode errorCode, String path) throws IOException {
        ErrorApi error = ApiFactory.error(errorCode, path).getBody();

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(mapper.writeValueAsString(error));
        response.flushBuffer();
    }
}
