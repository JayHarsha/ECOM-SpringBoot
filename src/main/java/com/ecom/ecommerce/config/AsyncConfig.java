package com.ecom.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration for Multi-Threading Asynchronous call
 */
@Configuration
@EnableAsync  // Enable asynchronous methods
public class AsyncConfig {
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // Minimum number of threads
        executor.setMaxPoolSize(20);    // Maximum number of threads
        executor.setQueueCapacity(25);  // Queue size for waiting tasks
        executor.setThreadNamePrefix("AsyncEmail-");
        executor.initialize();
        return executor;
    }
}
