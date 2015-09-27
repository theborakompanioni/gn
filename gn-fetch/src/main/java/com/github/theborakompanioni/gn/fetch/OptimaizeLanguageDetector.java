package com.github.theborakompanioni.gn.fetch;

import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by void on 22.09.15.
 */
public class OptimaizeLanguageDetector implements LanguageDetector {

    private com.optimaize.langdetect.LanguageDetector languageDetector;

    public OptimaizeLanguageDetector() throws IOException {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .build();
    }

    @Override
    public Optional<String> detect(String text) {
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

        TextObject textObject = textObjectFactory.forText(text);
        return languageDetector.detect(textObject)
                .transform(input -> Optional.ofNullable(input.getLanguage()))
                .or(Optional.empty());
    }
}
