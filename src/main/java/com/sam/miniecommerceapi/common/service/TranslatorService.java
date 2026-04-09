package com.sam.miniecommerceapi.common.service;

import java.util.Locale;

public interface TranslatorService {
	String translator(String code, Object[] args, Locale locale);
}
