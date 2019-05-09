package com.sun.health.newwork.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by 华硕 on 2019-05-09.
 */
@Component
public class Consumer {

    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    @JmsListener(destination = "test_queue", containerFactory = "queueListenerContainerFactory")
    public void listenTestQueue(Message message) {
        receive(message);
    }

    @JmsListener(destination = "test_topic", containerFactory = "topicListenerContainerFactory")
    public void listenTestTopic(Message message) {
        receive(message);
    }

    private void receive(Message message) {
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                logger.warn("接受: {}", ((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("只能接受文字消息");
        }
    }

}

