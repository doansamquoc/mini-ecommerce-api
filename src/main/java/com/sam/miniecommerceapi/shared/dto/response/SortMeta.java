package com.sam.miniecommerceapi.shared.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SortMeta {
    String sortBy;
    Sort.Direction direction;
}
