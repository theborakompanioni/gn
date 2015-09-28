package com.github.theborakompanioni.gn.article;

import gn.elastic.repository.ArticleElasticRepository;
import model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Created by void on 19.09.15.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private ArticleElasticRepository articleRepo;

    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ArticleServiceImpl(ArticleElasticRepository articleRepo, ElasticsearchTemplate elasticsearchTemplate) {
        this.articleRepo = articleRepo;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Optional<Article> findById(String articleId) {
        return Optional.ofNullable(articleRepo.findOne(articleId));
    }

    @Override
    public void deteleById(String articleId) {
        articleRepo.delete(articleId);
    }

    @Override
    public Page<Article> findAll(Pageable pageableOrNull) {
        final Pageable pageable = Optional.ofNullable(pageableOrNull)
                .orElse(new PageRequest(1, 100));

        return articleRepo.findAll(pageable);
    }

    @Override
    public Article save(Article article) {
        requireNonNull(article);
        requireNonNull(article.getUrl());

        if (article.getCreatedAt() == null) {
            article.setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        }

        return articleRepo.save(article);
    }

    @Override
    public void deleteAll() {
        //articleRepo.deleteAll(); <-- timeout expection - this is so f*cking slow..
        articleRepo.delete(articleRepo.findAll());
    }

    @Override
    public void verify(String articleId) {
        findById(articleId).ifPresent(article -> {
            article.setVerified(true);
            articleRepo.save(article);
        });
    }

    @Override
    public Page<Article> queryForPage(SearchQuery searchQuery) {
        return elasticsearchTemplate.queryForPage(searchQuery, Article.class);
    }

}
