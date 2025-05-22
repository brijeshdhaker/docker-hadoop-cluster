package org.examples.sb.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    //private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    /*

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new SimpleCacheManager();
        //Cache userCache = new ConcurrentMapCache("userCache");
        Cache roleCache = new ConcurrentMapCache("roleCache");
        cacheManager.setCaches(Arrays.asList(roleCache));
        return cacheManager;
    }
    */

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {

        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                log.info("Programmatically Customization of ConcurrentMapCacheManager ...");
                cacheManager.setCacheNames(Arrays.asList("userCache", "roleCache"));
            }
        };
    }


}
