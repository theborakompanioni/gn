package com.github.theborakompanioni.gn.article.events;

import com.github.theborakompanioni.gn.article.ArticleService;
import com.github.theborakompanioni.gn.fetch.Content;
import com.github.theborakompanioni.gn.fetch.ContentFetcher;
import com.google.common.base.Strings;
import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class ArticleCreateEventHandler implements EventHandler<ArticleCreateEvent> {

    private static final Logger log = LoggerFactory.
            getLogger(ArticleCreateEventHandler.class);

    private ArticleService articleService;
    private ContentFetcher contentFetcher;

    public ArticleCreateEventHandler(
            ArticleService articleService,
            ContentFetcher contentFetcher) {
        this.articleService = articleService;
        this.contentFetcher = contentFetcher;
    }

    @Transactional
    public void onEvent(ArticleCreateEvent event, long sequence, boolean endOfBatch) {
        articleService.findById(event.getArticleId())
                .ifPresent(article -> {
                    try {
                        final Content fetch = contentFetcher.fetch(article::getUrl);

                        article.setFetched(true);
                        article.setFetchedTitle(fetch.getTitle());
                        article.setFetchedDescription(fetch.getDescription());
                        article.setFetchedText(fetch.getText());
                        article.setFetchedVideoUrl(fetch.getVideoUrl());

                        if (Strings.isNullOrEmpty(article.getTitle())) {
                            article.setTitle(fetch.getTitle());
                        }
                        if (Strings.isNullOrEmpty(article.getDescription())) {
                            article.setDescription(fetch.getDescription());
                        }
                        if (Strings.isNullOrEmpty(article.getLanguage())) {
                            article.setLanguage(fetch.getLanguage());
                        }

                        articleService.save(article);
                    } catch (Exception e) {
                        log.error("", e);
                    }
                });
    }
}