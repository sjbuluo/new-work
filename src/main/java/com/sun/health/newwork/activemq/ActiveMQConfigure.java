package com.sun.health.newwork.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by 华硕 on 2019-05-09.
 */
@Configuration
@EnableJms
@EnableConfigurationProperties({CustomActiveMQProperties.class})
public class ActiveMQConfigure {

    private CustomActiveMQProperties customActiveMQProperties;

    public ActiveMQConfigure(CustomActiveMQProperties customActiveMQProperties) {
        this.customActiveMQProperties = customActiveMQProperties;
    }

    @Bean(name = "test_queue")
    public Queue queue() {
        return new ActiveMQQueue(customActiveMQProperties.getQueueName());
    }

    @Bean(name = "test_topic")
    public Topic topic() {
        return new ActiveMQTopic(customActiveMQProperties.getTopicName());
    }

    @Bean("queueListenerContainerFactory")
    public JmsListenerContainerFactory<?> queueListenerContainerFactory(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        defaultJmsListenerContainerFactory.setConnectionFactory(activeMQConnectionFactory);
        return defaultJmsListenerContainerFactory;

    }
    
    @Bean("topicListenerContainerFactory")
    public JmsListenerContainerFactory<?> topicListenerContainerFactory(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        defaultJmsListenerContainerFactory.setPubSubDomain(true);
        defaultJmsListenerContainerFactory.setConnectionFactory(activeMQConnectionFactory);
        return defaultJmsListenerContainerFactory;
    }

}
