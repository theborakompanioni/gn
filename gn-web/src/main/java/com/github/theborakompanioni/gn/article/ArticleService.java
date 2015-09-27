package com.github.theborakompanioni.gn.article;

import model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Optional;

/**
 * Created by void on 19.09.15.
 */
public interface ArticleService {

    void deteleById(String articleId);

    Optional<Article> findById(String articleId);

    Page<Article> findAll(Pageable pageableOrNull);

    Article save(Article article);

    void deleteAll();

    void verify(String articleId);

    Page<Article> queryForPage(SearchQuery searchQuery);
}
