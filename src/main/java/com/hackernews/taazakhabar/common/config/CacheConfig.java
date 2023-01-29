package com.hackernews.taazakhabar.common.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ObjectInputFilter;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${taazakhabar.client.hackernews.cache.ttl:15}")
    private int cacheLiveTime;

    @Bean
    Config config() {
        Config config = new Config();
        config.getMapConfigs().put("topStories", getConfig());
        return config;
    }

    private MapConfig getConfig(){
        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(cacheLiveTime * 60);
        return mapConfig;
    }
}
