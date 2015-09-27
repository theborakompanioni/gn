package com.github.theborakompanioni.gn.article.events;

import com.lmax.disruptor.EventFactory;

public class ArticleHeartEventFactory implements EventFactory<ArticleHeartEvent> {
    @Override
    public ArticleHeartEvent newInstance() {
        return new ArticleHeartEvent();
    }
}