package com.hackernews.taazakhabar.common;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ObjectInputFilter;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    Config config() {
        Config config = new Config();

        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(300);
        config.getMapConfigs().put("topStories", mapConfig);
        return config;
    }
}
