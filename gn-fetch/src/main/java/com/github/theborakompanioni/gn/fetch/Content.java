package com.github.theborakompanioni.gn.fetch;

import java.util.Collection;

/**
 * Created by void on 21.09.15.
 */
public interface Content {
     String getLanguage();

     String getImageUrl();

     String getTitle();

     String getUrl();

     String getOriginalUrl();

     String getCanonicalUrl();

     String getVideoUrl();

     String getRssUrl();

     String getText();

     String getFaviconUrl();

     String getDescription();

     String getDateString();

     Collection<String> getKeywords();

}
