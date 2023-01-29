package com.hackernews.taazakhabar.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadConfig {

    @Value("${taazakhabar.client.hackernews.threads.max-thread:0}")
    private int coreThreadPoolSize;

    @Bean
    public Executor executor(){
        if(coreThreadPoolSize == 0){
            System.out.println("From Cached Pool");
            return Executors.newCachedThreadPool();
        }
        else{
            System.out.println("From Fixed Pool");
            return Executors.newFixedThreadPool(coreThreadPoolSize);
        }
    }

}
