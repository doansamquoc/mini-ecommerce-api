package com.sam.miniecommerceapi.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationResponse<T> {
    List<T> items;
    int pageNo;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean first;
    boolean last;
}
