package com.github.theborakompanioni.gn.fetch;

import java.util.Optional;

/**
 * Created by void on 22.09.15.
 */
public interface LanguageDetector {
    Optional<String> detect(String text);
}
