package com.github.theborakompanioni.gn.article.events;

import com.lmax.disruptor.EventFactory;

public class ArticleCreateEventFactory implements EventFactory<ArticleCreateEvent> {
    @Override
    public ArticleCreateEvent newInstance() {
        return new ArticleCreateEvent();
    }
}