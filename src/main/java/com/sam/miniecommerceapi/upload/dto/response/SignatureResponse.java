package com.sam.miniecommerceapi.upload.dto.response;

public record SignatureResponse(
	long timestamp,
	String signature,
	String cloudName,
	String apiKey,
	String folder,
	String uploadPreset
) {}
