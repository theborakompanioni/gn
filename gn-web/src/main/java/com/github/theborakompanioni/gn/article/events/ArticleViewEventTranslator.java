package com.github.theborakompanioni.gn.article.events;

import com.lmax.disruptor.EventTranslator;

public class ArticleViewEventTranslator implements EventTranslator<ArticleViewEvent> {

    private String articleId;

    public ArticleViewEventTranslator(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public void translateTo(ArticleViewEvent articleViewEvent, long l) {
        articleViewEvent.setArticleId(articleId);
    }
}