package com.github.theborakompanioni.gn;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.GemFireCache;
import model.Permission;
import model.Role;
import model.User;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by void on 30.09.15.
 */
//@Configuration
@EnableGemfireRepositories(
        basePackageClasses = gn.geode._package.class
)
@EnableTransactionManagement
@EnableCaching
public class GeodeConfiguration {

    @Bean
    public CacheFactoryBean cacheFactoryBean() {
        return new CacheFactoryBean();
    }


    @Bean
    LocalRegionFactoryBean<String, User> defaultLocalRegionFactory(final GemFireCache cache) {
        return new LocalRegionFactoryBean<String, User>() {{
            setCache(cache);
            setName("default");
            setClose(false);
        }};
    }

    @Bean
    public GemfireCacheManager cacheManager(final Cache gemfireCache) {
        return new GemfireCacheManager() {{
            setCache(gemfireCache);
        }};
    }
}
