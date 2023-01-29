package com.hackernews.taazakhabar.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadConfig {

    @Bean
    public Executor executor(){
        return Executors.newCachedThreadPool();
    }
}
