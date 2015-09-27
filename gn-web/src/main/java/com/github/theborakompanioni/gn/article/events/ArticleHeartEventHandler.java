package com.github.theborakompanioni.gn.article.events;

import com.github.theborakompanioni.gn.article.ArticleService;
import com.lmax.disruptor.EventHandler;
import org.springframework.transaction.annotation.Transactional;

// TODO: rate limit / accumulate in memory
public class ArticleHeartEventHandler implements EventHandler<ArticleHeartEvent> {

    private ArticleService articleService;

    public ArticleHeartEventHandler(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Transactional
    public void onEvent(ArticleHeartEvent event, long sequence, boolean endOfBatch) {
        articleService.findById(event.getArticleId())
                .ifPresent(article -> {
                    article.setHearts(article.getHearts() + 1L);
                    articleService.save(article);
                });
    }
}