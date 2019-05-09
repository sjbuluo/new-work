package com.sun.health.newwork.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by 华硕 on 2019-05-07.
 */
@RabbitListener(queues = "hello", group = "gg")
@Component
public class RabbitMQHandler {

    private Logger logger = LoggerFactory.getLogger(RabbitMQHandler.class);

    @RabbitHandler
    public void handleHello(String msg) {
        logger.info("从hello Queue中接受: " + msg);
    }

}
