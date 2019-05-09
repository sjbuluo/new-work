package com.sun.health.newwork.base.controller;

import com.sun.health.newwork.activemq.Producer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 华硕 on 2019-05-07.
 */
@RestController
@RequestMapping("/base")
public class BaseController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/rabbitmq/send/hello/{msg}")
    public String sendToHelloQueue(@PathVariable("msg") String msg) {
        amqpTemplate.convertAndSend("hello", msg);
        return "将 [ " + msg +" ] 发送到RabbitMQ hello Queue中";
    }

    @Autowired
    private Producer producer;

    @RequestMapping("/activemq/send/{destination}")
    public String activeMQSend(@PathVariable("destination") String destination) {
        switch (destination) {
            case "queue":
                producer.sendToQueue();
                break;
            case "topic":
                producer.sendToTopic();
                break;
            default:
                break;
        }
        return "ok";
    }

}
