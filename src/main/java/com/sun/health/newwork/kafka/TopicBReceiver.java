package com.sun.health.newwork.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TopicBReceiver {

    private Logger logger = LoggerFactory.getLogger(TopicBReceiver.class);

    @KafkaListener(topics = {"b"})
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        logger.info("{} - {} : {}", consumerRecord.topic(), consumerRecord.key(), consumerRecord.value());
    }

}
