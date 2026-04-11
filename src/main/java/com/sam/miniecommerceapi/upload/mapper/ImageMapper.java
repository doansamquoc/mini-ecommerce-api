package com.sam.miniecommerceapi.upload.mapper;

import com.sam.miniecommerceapi.upload.dto.request.ImageAdditionRequest;
import com.sam.miniecommerceapi.upload.dto.response.ImageResponse;
import com.sam.miniecommerceapi.upload.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ImageMapper {
	Image toEntity(ImageAdditionRequest req);

	ImageResponse toResponse(Image image);
}
