package com.sam.miniecommerceapi.product.util;

import org.springframework.data.domain.Sort;

public class SortingUtils {
    public static Sort sort(String sortBy) {
        return switch (sortBy) {
            case "price-desc" -> Sort.by("minPrice").descending();
            case "price-asc" -> Sort.by("minPrice").ascending();
            case "newest" -> Sort.by("createdAt").descending();
            default -> Sort.by("name").ascending();
        };
    }
}
