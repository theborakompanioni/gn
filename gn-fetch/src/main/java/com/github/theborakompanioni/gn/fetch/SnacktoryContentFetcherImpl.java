package com.github.theborakompanioni.gn.fetch;

import de.jetwick.snacktory.HtmlFetcher;
import de.jetwick.snacktory.JResult;

/**
 * Created by void on 21.09.15.
 */
public class SnacktoryContentFetcherImpl implements ContentFetcher {
    private static int FETCH_TIMEOUT_IN_MILLISECONDS = 15_000;

    private HtmlFetcher fetcher;
    private HtmlCleaner cleaner;
    private LanguageDetector languageDetector;

    public SnacktoryContentFetcherImpl(HtmlFetcher fetcher, HtmlCleaner cleaner, LanguageDetector languageDetector) {
        this.fetcher = fetcher;
        this.cleaner = cleaner;
        this.languageDetector = languageDetector;
    }

    @Override
    public Content fetch(ContentFetchContext context) throws Exception {

        final JResult jResult = fetcher.fetchAndExtract(context.getUrl(), FETCH_TIMEOUT_IN_MILLISECONDS, true);

        return createContent(jResult);
    }

    private Content createContent(JResult jResult) {
        SimpleContent newContent = new SimpleContent();
        newContent.setUrl(clean(jResult.getUrl()));
        newContent.setTitle(clean(jResult.getTitle()));
        newContent.setDescription(clean(jResult.getDescription()));
        newContent.setText(clean(jResult.getText()));
        newContent.setDateString(clean(jResult.getDate()));
        newContent.setVideoUrl(clean(jResult.getVideoUrl()));
        newContent.setKeywords(jResult.getKeywords());

        languageDetector.detect(newContent.getText())
                .ifPresent(newContent::setLanguage);

        return newContent;
    }

    private String clean(String html) {
        return cleaner.clean(html);
    }
}
