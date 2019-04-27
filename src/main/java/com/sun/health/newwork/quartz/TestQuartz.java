package com.sun.health.newwork.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class TestQuartz extends QuartzJobBean{

    private Logger logger = LoggerFactory.getLogger(QuartzJobBean.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        String key = mergedJobDataMap.getString("key");
        logger.info("{} quartz task: {}", key, new Date());
    }
}
