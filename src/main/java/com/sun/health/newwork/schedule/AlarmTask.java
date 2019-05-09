package com.sun.health.newwork.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableScheduling
//@Async
public class AlarmTask {

    private Logger logger = LoggerFactory.getLogger(AlarmTask.class);

    @Scheduled(cron = "0/5 * * * * *")
    public void run() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        logger.info(Thread.currentThread().getName() + "-----> 使用cron {}", System.currentTimeMillis() / 1000);
    }

    @Scheduled(fixedRate = 5000)
    public void runAtFixedRate() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        logger.info(Thread.currentThread().getName() + "-----> 使用fixedRate {}", System.currentTimeMillis() / 1000);
    }

    @Scheduled(fixedDelay = 5000)
    public void runWithFixedDelay() throws InterruptedException {
        TimeUnit.SECONDS.sleep(7);
        logger.info(Thread.currentThread().getName() + "-----> 使用fixedDelay {}", System.currentTimeMillis() / 1000);
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void runWithInitDelayAndFixedDelay() throws InterruptedException {
        logger.info(Thread.currentThread().getName() + "-----> 使用initDelay And fixedDelay {}", System.currentTimeMillis() / 1000);
    }

}
