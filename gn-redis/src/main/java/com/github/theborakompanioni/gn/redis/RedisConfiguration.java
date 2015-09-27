package com.github.theborakompanioni.gn.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

/**
 * Created by void on 21.09.15.
 */
@Configuration
public class RedisConfiguration {

    //@Bean(initMethod = "start", destroyMethod = "stop")
    public RedisServer redisServer() {
        return RedisServer.builder()
                .port(6379)
                .build();
    }
}
