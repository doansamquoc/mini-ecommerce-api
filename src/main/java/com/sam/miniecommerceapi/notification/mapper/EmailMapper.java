package com.sam.miniecommerceapi.notification.mapper;

import com.sam.miniecommerceapi.notification.dto.request.EmailRequest;
import com.sam.miniecommerceapi.notification.dto.request.TemplateEmailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmailMapper {
    EmailRequest toEmailRequest(TemplateEmailRequest request);
}
