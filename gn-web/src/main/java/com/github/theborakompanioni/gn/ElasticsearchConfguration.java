package com.github.theborakompanioni.gn;

import gn.elastic.repository._package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

@Configuration
@EnableElasticsearchRepositories(
        basePackageClasses = _package.class
)
public class ElasticsearchConfguration {

    private static final Logger log = LoggerFactory.
            getLogger(ElasticsearchConfguration.class);

    @Autowired
    private ElasticsearchProperties properties;

    @PostConstruct
    public void postConstruct() {
        log.info("{}", properties);
    }

}