package com.sam.miniecommerceapi.common.service.impl;

import com.sam.miniecommerceapi.common.service.TranslatorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranslatorServiceImpl implements TranslatorService {
	MessageSource messageSource;

	@Override
	public String translator(String code, Object[] args, Locale locale) {
		return messageSource.getMessage(code, args, code, locale);
	}
}
