package com.github.theborakompanioni.gn.fetch;

/**
 * Created by void on 22.09.15.
 */
@FunctionalInterface
public interface HtmlCleaner {
    String clean(String html);
}
