package gn.geode;

import com.gemstone.gemfire.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.support.GemfireCacheManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by void on 30.09.15.
 */
@EnableGemfireRepositories(
        basePackageClasses = gn.geode._package.class
)
@EnableTransactionManagement
public interface GeodeConfiguration {

    @Bean
    default CacheFactoryBean cacheFactoryBean() {
        return new CacheFactoryBean();
    }

    @Bean
    default GemfireCacheManager cacheManager(final Cache gemfireCache) {
        return new GemfireCacheManager() {{
            setCache(gemfireCache);
        }};
    }
}
