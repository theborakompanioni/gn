package com.github.theborakompanioni.gn.fetch;

/**
 * Created by void on 21.09.15.
 */
public interface ContentFetcher {
    Content fetch(ContentFetchContext context) throws Exception;
}
