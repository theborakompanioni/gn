package com.github.theborakompanioni.gn;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(
        basePackageClasses = gn.elastic.repository._package.class
)
public class ElasticsearchConfguration {

}