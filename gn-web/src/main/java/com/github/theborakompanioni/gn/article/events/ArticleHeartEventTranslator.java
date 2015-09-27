package com.github.theborakompanioni.gn.article.events;

import com.lmax.disruptor.EventTranslator;

public class ArticleHeartEventTranslator implements EventTranslator<ArticleHeartEvent> {

    private String articleId;

    public ArticleHeartEventTranslator(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public void translateTo(ArticleHeartEvent event, long l) {
        event.setArticleId(articleId);
    }
}