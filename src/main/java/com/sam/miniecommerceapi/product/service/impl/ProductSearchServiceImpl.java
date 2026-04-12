package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.common.dto.response.PageResponse;
import com.sam.miniecommerceapi.product.dto.SearchDTO;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.service.ProductSearchService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductSearchServiceImpl implements ProductSearchService {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public PageResponse<SearchDTO> searchProducts(
		String keyword,
		BigDecimal minPrice,
		BigDecimal maxPrice,
		String catName,
		Pageable pageable
	) {
		SearchSession searchSession = Search.session(entityManager);
		SearchResult<SearchDTO> result = searchSession.search(Product.class).select(SearchDTO.class).where(f -> {
			boolean hasKeyword = keyword != null && !keyword.isBlank();
			boolean hasCategory = catName != null && !catName.isBlank();
			boolean hasMinPrice = minPrice != null;
			boolean hasMaxPrice = maxPrice != null;

			if (!hasKeyword && !hasCategory && !hasMinPrice) return f.matchAll();
			var bool = f.bool();

			if (hasKeyword) {
				bool.must(
					f.match()
						.field("name").boost(2.0f) // Important than description
						.field("description")
						.matching(keyword)
						.fuzzy(1)
				);
			}

			if (hasMinPrice || hasMaxPrice) {
				bool.must(f.range().field("regularPrice").between(minPrice, maxPrice).toPredicate());
			}

			if (hasCategory) {
				bool.must(f.match().field("category.name").matching(catName).toPredicate());
			}
			return bool;
		}).fetch(pageable.getPageNumber(), pageable.getPageSize());
		return PageResponse.fromSearchResult(result.hits(), result.total().hitCount(), pageable);
	}
}

