package com.github.theborakompanioni.gn.article.events;

public class ArticleCreateEvent {
    private String articleId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}