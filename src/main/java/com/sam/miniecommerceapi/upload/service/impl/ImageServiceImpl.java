package com.sam.miniecommerceapi.upload.service.impl;

import com.sam.miniecommerceapi.common.constant.ErrorCode;
import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.common.exception.BusinessException;
import com.sam.miniecommerceapi.upload.dto.request.ImageAdditionRequest;
import com.sam.miniecommerceapi.upload.dto.response.ImageResponse;
import com.sam.miniecommerceapi.upload.entity.Image;
import com.sam.miniecommerceapi.upload.mapper.ImageMapper;
import com.sam.miniecommerceapi.upload.repository.ImageRepository;
import com.sam.miniecommerceapi.upload.service.ImageService;
import com.sam.miniecommerceapi.upload.service.UploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {
	UploadService uploadService;
	ImageMapper mapper;
	ImageRepository repository;

	@Override
	public ImageResponse createImage(ImageAdditionRequest req) {
		Image image = mapper.toEntity(req);
		return mapper.toResponse(save(image));
	}

	@Override
	public ImageResponse getImage(Long id) {
		return mapper.toResponse(findById(id));
	}

	@Override
	public PageResponse<ImageResponse> getImages(Pageable pageable) {
		Page<Image> images = findAll(pageable);
		Page<ImageResponse> responses = images.map(mapper::toResponse);
		return PageResponse.from(responses);
	}

	@Transactional
	@Override
	public void deleteImage(Long id) {
		Image image = findById(id);
		uploadService.delete(image.getPublicId());
		delete(id);
	}

	@Override
	public Image getReference(Long id) {
		return repository.getReferenceById(id);
	}

	private Image save(Image image) {
		return repository.save(image);
	}

	@Override
	public Image findById(Long id) {
		return repository.findById(id).orElseThrow(() -> BusinessException.of(ErrorCode.IMAGE_NOT_FOUND));
	}

	private Page<Image> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	private void delete(Long id) {
		repository.deleteById(id);
	}
}
