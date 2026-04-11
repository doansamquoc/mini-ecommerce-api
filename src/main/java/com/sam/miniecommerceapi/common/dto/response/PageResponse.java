package com.sam.miniecommerceapi.common.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
	List<T> content;
	PageMeta pageMeta;

	public static <T> PageResponse<T> from(Page<T> page) {
		Sort.Order order = page.getSort().stream().findFirst().orElse(null);
		List<SortMeta> sortsMeta = null;

		if (order != null) {
			sortsMeta = Collections.singletonList(SortMeta.builder()
				.sortBy(order.getProperty())
				.direction(order.getDirection())
				.build());
		}

		PageMeta meta = PageMeta.builder()
			.pageNumber(page.getNumber())
			.pageSize(page.getSize())
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.sort(sortsMeta)
			.build();

		return PageResponse.<T>builder().content(page.getContent()).pageMeta(meta).build();
	}

	public static <T> PageResponse<T> fromSearchResult(List<T> content, long totalElements, Pageable pageable) {
		int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
		PageMeta meta = PageMeta.builder()
			.currentPage(pageable.getPageNumber() + 1)
			.pageNumber(pageable.getPageNumber())
			.pageSize(pageable.getPageSize())
			.totalElements(totalElements)
			.totalPages(totalPages)
			.build();

		return PageResponse.<T>builder().content(content).pageMeta(meta).build();
	}
}
