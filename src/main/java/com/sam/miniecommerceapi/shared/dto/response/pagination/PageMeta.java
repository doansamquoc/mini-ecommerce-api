package com.sam.miniecommerceapi.shared.dto.response.pagination;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageMeta {
    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
    List<SortMeta> sort;
}
