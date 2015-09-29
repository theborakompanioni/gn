package com.github.theborakompanioni.gn.article;

import com.github.theborakompanioni.gn.article.events.ArticleCreateEvent;
import com.github.theborakompanioni.gn.article.events.ArticleCreateEventTranslator;
import com.github.theborakompanioni.gn.article.events.ArticleHeartEvent;
import com.github.theborakompanioni.gn.article.events.ArticleHeartEventTranslator;
import com.github.theborakompanioni.gn.util.ForwardingPage;
import com.google.common.util.concurrent.RateLimiter;
import com.lmax.disruptor.dsl.Disruptor;
import model.Article;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@RestController
@RequestMapping("/articles")
public class ArticleRestCtrl {
    private static int DEFAULT_PAGE_SIZE = 100;

    private static final Logger log = LoggerFactory.
            getLogger(ArticleRestCtrl.class);

    RateLimiter heartEventRateLimiter = RateLimiter.create(1);

    @Autowired
    private Disruptor<ArticleHeartEvent> heartEventDisruptor;

    @Autowired
    private Disruptor<ArticleCreateEvent> createEventDisruptor;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ArticleResource>> getAll(final Pageable pageableOrNull) {
        final Page<ArticleResource> page = fetchAsArticleResourcePage(pageableOrNull);

        return ResponseEntity.ok(page);
    }

    @RequestMapping(value = "/newest", method = RequestMethod.GET)
    public ResponseEntity<Page<ArticleResource>> newest(final Pageable pageableOrNull) {
        final Pageable pageable = getPageableOrDefault(pageableOrNull);

        SearchQuery searchByCreatedAtQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(SortBuilders.fieldSort("createdAt").order(SortOrder.DESC))
                .withFilter(boolFilter().must(termFilter("verified", true)))
                .withPageable(pageable)
                .build();

        final Page<Article> articlePage = articleService.queryForPage(searchByCreatedAtQuery);

        final Page<ArticleResource> page = asArticleResourcePage(articlePage);

        return ResponseEntity.ok(page);
    }


    @RequestMapping(value = "/popular", method = RequestMethod.GET)
    public ResponseEntity<Page<ArticleResource>> popular(final Pageable pageableOrNull) {
        final Pageable pageable = getPageableOrDefault(pageableOrNull);

        SearchQuery searchByHeartsQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(SortBuilders.fieldSort("hearts").order(SortOrder.DESC))
                .withFilter(boolFilter().must(termFilter("verified", true)))
                .withPageable(pageable)
                .build();

        final Page<Article> articlePage = articleService.queryForPage(searchByHeartsQuery);

        final Page<ArticleResource> page = asArticleResourcePage(articlePage);

        return ResponseEntity.ok(page);
    }


    @RequestMapping(value = "/verification", method = RequestMethod.GET)
    public ResponseEntity<Page<ArticleResource>> verification(final Pageable pageableOrNull) {
        final Pageable pageable = getPageableOrDefault(pageableOrNull);

        SearchQuery searchByVerficationQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(SortBuilders.fieldSort("createdAt").order(SortOrder.ASC))
                .withFilter(boolFilter().must(termFilter("verified", false)))
                .withPageable(pageable)
                .build();

        final Page<Article> articlePage = articleService.queryForPage(searchByVerficationQuery);

        final Page<ArticleResource> page = asArticleResourcePage(articlePage);

        return ResponseEntity.ok(page);
    }

    private Pageable getPageableOrDefault(Pageable pageableOrNull) {
        return Optional.ofNullable(pageableOrNull)
                .orElse(new PageRequest(0, DEFAULT_PAGE_SIZE));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Article> create(@RequestBody final Article article) {

        article.setFetched(false);

        if (SecurityUtils.getSubject().isPermitted("article:verify")) {
            article.setVerified(false);
        }

        if (article.getCreatedBy() == null) {
            article.setCreatedBy(Optional.ofNullable(SecurityUtils.getSubject())
                    .map(Subject::getSession)
                    .map(session -> (String) session.getAttribute("email"))
                    .map(email -> email.substring(0, email.indexOf('@')))
                    .orElse("anonymous"));
        }

        Article savedArticle = articleService.save(article);

        Optional.of(savedArticle)
                .map(Article::getId)
                .map(ArticleCreateEventTranslator::new)
                .ifPresent(createEventDisruptor::publishEvent);

        return ResponseEntity.ok(savedArticle);
    }

    @RequestMapping(value = "/{articleId}", method = RequestMethod.GET)
    public ResponseEntity<Article> getOne(@PathVariable String articleId) {
        return articleService.findById(articleId).map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequiresAuthentication
    @RequiresPermissions("article:delete")
    @RequestMapping(value = "/{articleId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteOne(@PathVariable String articleId) {
        articleService.deteleById(articleId);
        return ResponseEntity.noContent().build();
    }

    @RequiresAuthentication
    @RequiresPermissions("article:verify")
    @RequestMapping(value = "/{articleId}/verify", method = RequestMethod.PUT)
    public ResponseEntity<Void> verify(@PathVariable String articleId) {
        articleService.verify(articleId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{articleId}/heart", method = RequestMethod.POST)
    public ResponseEntity<Void> heart(@PathVariable String articleId) {

        requireNonNull(articleId);

        final Optional<Article> articleOrEmpty = articleService.findById(articleId);

        articleOrEmpty.filter(foo -> heartEventRateLimiter.tryAcquire())
                .map(Article::getId)
                .map(ArticleHeartEventTranslator::new)
                .ifPresent(heartEventDisruptor::publishEvent);

        return articleOrEmpty.map(article -> ResponseEntity.accepted().build())
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private Page<ArticleResource> fetchAsArticleResourcePage(Pageable pageableOrNull) {
        final Pageable pageable = Optional.ofNullable(pageableOrNull)
                .orElse(new PageRequest(1, DEFAULT_PAGE_SIZE));

        final Page<Article> all = articleService.findAll(pageable);

        return asArticleResourcePage(all);
    }

    private Page<ArticleResource> asArticleResourcePage(Page<Article> articlePage) {
        final List<ArticleResource> allAsResource = articlePage.getContent().stream()
                .map(ArticleResource::new)
                .collect(toList());

        final Page<ArticleResource> forwarding = ForwardingPage.forwarding(allAsResource, articlePage);

        return forwarding;
    }
}
