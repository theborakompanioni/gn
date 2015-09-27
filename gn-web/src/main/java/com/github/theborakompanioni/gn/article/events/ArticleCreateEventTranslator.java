package com.github.theborakompanioni.gn.article.events;

import com.lmax.disruptor.EventTranslator;

public class ArticleCreateEventTranslator implements EventTranslator<ArticleCreateEvent> {

    private String articleId;

    public ArticleCreateEventTranslator(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public void translateTo(ArticleCreateEvent event, long l) {
        event.setArticleId(articleId);
    }
}