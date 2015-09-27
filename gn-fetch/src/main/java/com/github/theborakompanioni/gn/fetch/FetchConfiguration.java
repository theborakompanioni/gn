package com.github.theborakompanioni.gn.fetch;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;
import de.jetwick.snacktory.SCache;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.nullToEmpty;

/**
 * Created by void on 21.09.15.
 */
@Configuration
public class FetchConfiguration {

    @Bean
    public LanguageDetector languageDetector() throws IOException {
        return new OptimaizeLanguageDetector();
    }

    @Bean
    public HtmlCleaner htmlCleaner() {
        return (html) -> Jsoup.clean(nullToEmpty(html), Whitelist.none());
    }

    @Bean
    public ContentFetcher contentFetcher() throws IOException {
        ContentFetcher fetcher = new SnacktoryContentFetcherImpl
                (htmlFetcher(), htmlCleaner(), languageDetector());

        return fetcher;
    }

    @Bean
    public HtmlFetcher htmlFetcher() {
        HtmlFetcher fetcher = new HtmlFetcher();

        fetcher.setCache(htmlFetcherCache());

        return fetcher;
    }

    @Bean
    public SCache htmlFetcherCache() {
        final Cache<String, JResult> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .concurrencyLevel(20)
                .maximumSize(10_000)
                .build();

        return new SCache() {
            @Override
            public JResult get(String s) {
                return cache.getIfPresent(s);
            }

            @Override
            public void put(String s, JResult jResult) {
                cache.put(s, jResult);
            }

            @Override
            public int getSize() {
                return (int) cache.size();
            }
        };
    }
}
