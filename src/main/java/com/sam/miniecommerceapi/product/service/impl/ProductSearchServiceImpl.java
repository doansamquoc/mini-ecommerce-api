package com.sam.miniecommerceapi.product.service.impl;

import com.sam.miniecommerceapi.product.dto.response.ProductResponse;
import com.sam.miniecommerceapi.product.entity.Product;
import com.sam.miniecommerceapi.product.service.ProductSearchService;
import com.sam.miniecommerceapi.shared.dto.response.pagination.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductSearchServiceImpl implements ProductSearchService {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public PageResponse<ProductResponse> searchProducts(
            String keyword,
            BigDecimal minPrice,
            String categoryName,
            Pageable pageable
    ) {
        SearchSession searchSession = Search.session(entityManager);

        var result = searchSession.search(Product.class).where(f -> {

            var bool = f.bool();
            boolean hasCondition = false;

            if (keyword != null && !keyword.isBlank()) {
                int fuzziness = keyword.length() > 3 ? 2 : 0;
                bool.must(f.match()
                        .fields("name", "description")
                        .matching(keyword)
                        .fuzzy(fuzziness)
                        .toPredicate()
                );
                hasCondition = true;
            }

            if (minPrice != null) {
                bool.must(f.match().field("minPrice").matching(minPrice).toPredicate());
                hasCondition = true;
            }

            if (categoryName != null && !categoryName.isBlank()) {
                bool.must(f.match().field("category.name").matching(categoryName).toPredicate());
                hasCondition = true;
            }

            if (!hasCondition) return f.matchAll();

            return bool;

        }).fetch((int) pageable.getOffset(), pageable.getPageSize());

        List<ProductResponse> content = result.hits().stream().map(this::toResponse).toList();
        return PageResponse.fromSearchResult(content, result.total().hitCount(), pageable);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .minPrice(product.getMinPrice())
                .slug(product.getSlug())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }
}
