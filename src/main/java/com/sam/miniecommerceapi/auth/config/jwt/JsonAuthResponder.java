package com.sam.miniecommerceapi.auth.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.exception.ErrorResponse;
import com.sam.miniecommerceapi.common.service.TranslatorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonAuthResponder implements AuthResponder {
	ObjectMapper mapper;
	TranslatorService translator;

	@Override
	public void sendError(HttpServletResponse response, ErrorCode ec, String path) throws IOException {
		Locale locale = LocaleContextHolder.getLocale();
		String messaged = translator.translator(ec.getMessageKey(), null, locale);
		ErrorResponse error = ErrorResponse.of(ec.getStatus(), ec.getCode(), messaged);

		response.setStatus(ec.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		response.getWriter().write(mapper.writeValueAsString(error));
		response.flushBuffer();
	}
}
