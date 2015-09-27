package com.github.theborakompanioni.gn.article.events;

import com.lmax.disruptor.EventFactory;

public class ArticleViewEventFactory implements EventFactory<ArticleViewEvent> {
    @Override
    public ArticleViewEvent newInstance() {
        return new ArticleViewEvent();
    }
}