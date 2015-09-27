package com.github.theborakompanioni.gn.article.events;

import com.github.theborakompanioni.gn.article.ArticleService;
import com.lmax.disruptor.EventHandler;
import org.springframework.transaction.annotation.Transactional;

// TODO: rate limit / accumulate in memory
public class ArticleViewEventHandler implements EventHandler<ArticleViewEvent> {

    private ArticleService articleService;

    public ArticleViewEventHandler(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Transactional
    public void onEvent(ArticleViewEvent event, long sequence, boolean endOfBatch) {
        articleService.findById(event.getArticleId())
                .ifPresent(article -> {
                    article.setViews(article.getViews() + 1L);
                    articleService.save(article);
                });
    }
}