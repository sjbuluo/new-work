package com.sun.health.newwork.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//@Configuration
//@EnableAsync
public class AsynConfig {

    @Value("${custom.schedule.async.corePoolSize}")
    private int corePoolSize;

    @Value("${custom.schedule.async.maxPoolSize}")
    private int maxPoolSize;

    @Value("${custom.schedule.async.queueCapacity}")
    private int queueCapacity;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
