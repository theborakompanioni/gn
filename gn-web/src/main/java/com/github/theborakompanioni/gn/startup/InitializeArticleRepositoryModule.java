package com.github.theborakompanioni.gn.startup;

import com.github.theborakompanioni.gn.article.ArticleService;
import model.Article;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Created by void on 22.08.15.
 */
@Configuration
@DependsOn("securityManager")
@Profile("development")
public class InitializeArticleRepositoryModule {
    private static final Logger log = LoggerFactory.
            getLogger(InitializeArticleRepositoryModule.class);

    @Bean
    public InitializingBean initializeArticleRepository(SecurityManager securityManager, ArticleService articleService) {
        return () -> {
            articleService.deleteAll();

            try {
                SecurityUtils.getSecurityManager();
            } catch (Throwable t) {
                log.info("Initializing security manager manually.");
                SecurityUtils.setSecurityManager(securityManager);
            }

            log.info("Initializing article scenario..");

            IntStream.range(0, 400).boxed().map(i -> {
                final Article article = new Article();
                article.setTitle("title " + i);
                article.setUrl("http://www.example.com/" + i);
                article.setCreatedAt(
                        LocalDateTime.now()
                                .minusDays(ThreadLocalRandom.current().nextInt(1000))
                                .toEpochSecond(ZoneOffset.UTC)
                );
                article.setVerified(ThreadLocalRandom.current().nextBoolean());
                return article;
            }).forEach(articleService::save);
        };
    }

}
