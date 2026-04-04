package com.sam.miniecommerceapi.init;

import com.sam.miniecommerceapi.product.entity.Product;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConditionalOnBooleanProperty(name = "app.search.reindex-on-startup")
public class HibernateSearchIndexer implements CommandLineRunner {
    EntityManager manager;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            log.info("Starting mass indexing...");
            SearchSession searchSession = Search.session(manager);
            MassIndexer indexer = searchSession.massIndexer(Product.class).threadsToLoadObjects(7);
            indexer.startAndWait();
            log.info("Mass indexing completed!");
        } catch (InterruptedException ie) {
            log.error("Indexing was interrupted", ie);
            Thread.currentThread().interrupt();
        }
    }
}
