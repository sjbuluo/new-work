package com.sun.health.newwork.rabbitmq.work_model;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.sun.health.newwork.rabbitmq.util.RabbitMQConnectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.sun.health.newwork.rabbitmq.work_model.WorkModelConfig.*;

public class WorkModelSender {

    private Logger logger = LoggerFactory.getLogger(WorkModelSender.class);

    @Test
    public void testSend() throws IOException, TimeoutException {
        Connection connection = RabbitMQConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(WORK_MODEL_QUEUE, false, false, false, null);
        channel.exchangeDeclare(WORK_MODEL_EXCHANGE, BuiltinExchangeType.TOPIC, false, false, null);
        channel.queueBind(WORK_MODEL_QUEUE, WORK_MODEL_EXCHANGE, WORK_MODEL_BINDING_KEY);

        for (int i = 0; i < 10; i++) {
            channel.basicPublish(WORK_MODEL_EXCHANGE, WORK_MODEL_ROUTING_KEY, null, ("Hello " + i).getBytes());
        }
    }

}
