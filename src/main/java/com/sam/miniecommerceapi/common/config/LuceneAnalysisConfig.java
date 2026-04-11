package com.sam.miniecommerceapi.common.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LuceneAnalysisConfig implements LuceneAnalysisConfigurer {
	@Override
	public void configure(LuceneAnalysisConfigurationContext context) {
		log.info("Lucene Analysis Configured");
		context.analyzer("name_analyzer").custom().tokenizer("standard").tokenFilter("lowercase").tokenFilter("asciiFolding");
	}
}
