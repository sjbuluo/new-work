package com.sun.health.newwork.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by 华硕 on 2019-05-09.
 */
@Service
public class ProducerImpl implements Producer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("test_queue")
    private Queue queue;

    @Autowired
    @Qualifier("test_topic")
    private Topic topic;

    public void sendToQueue() {
        jmsMessagingTemplate.convertAndSend(queue, "Send Hello Message from sendToQueue()");
    }

    public void sendToTopic() {
        jmsMessagingTemplate.convertAndSend(topic, "Send Hello Message from sendToTopic");
    }

}
