package com.github.theborakompanioni.gn.article;

import com.github.theborakompanioni.gn.article.events.ArticleViewEvent;
import com.github.theborakompanioni.gn.article.events.ArticleViewEventTranslator;
import com.google.common.util.concurrent.RateLimiter;
import com.lmax.disruptor.dsl.Disruptor;
import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/articles")
public class ArticleCtrl {
    private static final Logger log = LoggerFactory.
            getLogger(ArticleCtrl.class);

    RateLimiter viewEventRateLimiter = RateLimiter.create(1);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Disruptor<ArticleViewEvent> disruptor;

    @RequestMapping(value = "/view/{articleId}", method = RequestMethod.GET)
    public ResponseEntity<?> view(@PathVariable final String articleId) {
        requireNonNull(articleId);

        final Optional<Article> articleOrEmpty = articleService.findById(articleId);

        articleOrEmpty.filter(foo -> viewEventRateLimiter.tryAcquire())
                .map(Article::getId)
                .map(ArticleViewEventTranslator::new)
                .ifPresent(disruptor::publishEvent);

        return articleOrEmpty.map(article -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, article.getUrl());
            return new ResponseEntity<>(null, headers, HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}
