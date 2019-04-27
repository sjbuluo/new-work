package com.sun.health.newwork.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail createJobDetail() {
        JobDetail testQuartz = JobBuilder.newJob(TestQuartz.class).withIdentity("testQuartz").storeDurably().build();
        testQuartz.getJobDataMap().put("key", "suj");
        return testQuartz;
    }

    @Bean
    public Trigger createQuartzTrigger() {
//        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
//        return TriggerBuilder.newTrigger().forJob(createJobDetail()).withIdentity("testQuartz").withSchedule(simpleScheduleBuilder).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        return TriggerBuilder.newTrigger().forJob(createJobDetail()).withIdentity("testQuartz").withSchedule(cronScheduleBuilder).build();
    }

}
