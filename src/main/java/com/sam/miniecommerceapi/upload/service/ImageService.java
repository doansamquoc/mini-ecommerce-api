package com.sam.miniecommerceapi.upload.service;

import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.upload.dto.request.ImageAdditionRequest;
import com.sam.miniecommerceapi.upload.dto.response.ImageResponse;
import com.sam.miniecommerceapi.upload.entity.Image;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface ImageService {
	ImageResponse createImage(ImageAdditionRequest req);

	ImageResponse getImage(Long id);

	PageResponse<ImageResponse> getImages(Pageable pageable);

	@Transactional
	void deleteImage(Long id);

	Image getReference(Long id);

	Image findById(Long id);
}
