package com.github.theborakompanioni.gn.article;

import com.github.theborakompanioni.gn.article.events.*;
import com.github.theborakompanioni.gn.fetch.ContentFetcher;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by void on 20.09.15.
 */
@Configuration
public class ArticleConfiguration {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService disruptorExecutor() {
        return Executors.newWorkStealingPool();
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService disruptorArticleCreateExecutor() {
        return Executors.newWorkStealingPool();
    }

    @Bean
    public ArticleViewEventFactory articleViewEventFactory() {
        return new ArticleViewEventFactory();
    }

    @Bean
    public ArticleViewEventHandler articleViewEventHandler(ArticleService articleService) {
        return new ArticleViewEventHandler(articleService);
    }

    @Bean(destroyMethod = "shutdown")
    public Disruptor<ArticleViewEvent> viewEventDisruptor(ArticleViewEventHandler articleViewEventHandler) {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 32768;

        // Construct the Disruptor
        Disruptor<ArticleViewEvent> disruptor = new Disruptor<>(
                articleViewEventFactory(), bufferSize, disruptorExecutor());


        // Connect the handler
        disruptor.handleEventsWith(articleViewEventHandler);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        return disruptor;
    }


    @Bean
    public ArticleHeartEventFactory articleHeartEventFactory() {
        return new ArticleHeartEventFactory();
    }

    @Bean
    public ArticleHeartEventHandler articleHeartEventHandler(ArticleService articleService) {
        return new ArticleHeartEventHandler(articleService);
    }

    @Bean(destroyMethod = "shutdown")
    public Disruptor<ArticleHeartEvent> heartEventDisruptor(ArticleHeartEventHandler articleHeartEventHandler) {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 4096;

        // Construct the Disruptor
        Disruptor<ArticleHeartEvent> disruptor = new Disruptor<>(
                articleHeartEventFactory(), bufferSize, disruptorExecutor());

        // Connect the handler
        disruptor.handleEventsWith(articleHeartEventHandler);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        return disruptor;
    }


    @Bean
    public ArticleCreateEventFactory articleCreateEventFactory() {
        return new ArticleCreateEventFactory();
    }

    @Bean
    public ArticleCreateEventHandler articleCreateEventHandler(ArticleService articleService, ContentFetcher fetcher) {
        return new ArticleCreateEventHandler(articleService, fetcher);
    }

    @Bean(destroyMethod = "shutdown")
    public Disruptor<ArticleCreateEvent> createEventDisruptor(ArticleCreateEventHandler articleCreateEventHandler) {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 32768;

        // Construct the Disruptor
        Disruptor<ArticleCreateEvent> disruptor = new Disruptor<>(
                articleCreateEventFactory(), bufferSize, disruptorArticleCreateExecutor());

        // Connect the handler
        disruptor.handleEventsWith(articleCreateEventHandler);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        return disruptor;
    }


}
