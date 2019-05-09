package com.sun.health.newwork.activemq;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 华硕 on 2019-05-09.
 */
@ConfigurationProperties(prefix = "custom.activemq")
public class CustomActiveMQProperties {

    private String queueName;

    private String topicName;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
