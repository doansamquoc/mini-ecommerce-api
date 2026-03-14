package com.sam.miniecommerceapi.common.dto.response.pagination;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageMeta {
    int page;
    int size;
    long totalElements;
    int totalPages;
}
