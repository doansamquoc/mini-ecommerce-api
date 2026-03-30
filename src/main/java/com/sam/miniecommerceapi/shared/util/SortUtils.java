package com.sam.miniecommerceapi.shared.util;

import org.springframework.data.domain.Sort;

public class SortUtils {
    /**
     * Extract sort from string
     *
     * @param sort Sort string like "[conditional], [order direction]".
     *             Please remember the space in the param after the comma.
     * @return Sort
     */
    public static Sort extractSortFromString(String sort) {
        String[] sortParts = sort.split(", ");
        return Sort.by(Sort.Direction.fromString(sortParts[1]), sortParts[0]);
    }
}
