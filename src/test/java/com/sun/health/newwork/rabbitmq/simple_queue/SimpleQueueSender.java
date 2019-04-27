package com.sun.health.newwork.rabbitmq.simple_queue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.sun.health.newwork.rabbitmq.util.RabbitMQConnectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.sun.health.newwork.rabbitmq.simple_queue.SimpleQueueConfig.*;

public class SimpleQueueSender {

    private Logger logger = LoggerFactory.getLogger(SimpleQueueSender.class);

    @Test
    public void testSend() throws IOException, TimeoutException {
        // 获取连接以及mq通道
        Connection connection = RabbitMQConnectionUtils.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(SIMPLE_QUEUE_NAME, false, false, false, null);

        channel.exchangeDeclare(SIMPLE_QUEUE_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, null);

        channel.queueBind(SIMPLE_QUEUE_NAME, SIMPLE_QUEUE_EXCHANGE_NAME, SIMPLE_QUEUE_BINDING_KEY, null);

        // 消息内容
        String payload = "Hello World!";
        // 发布消息
//        channel.basicPublish("", SIMPLE_QUEUE_NAME, null, payload.getBytes());
        channel.basicPublish(SIMPLE_QUEUE_EXCHANGE_NAME, SIMPLE_QUEUE_ROUTING_KEY, null, payload.getBytes());

        logger.info("SimpleQueueSender send: " + payload);
        channel.close();
        connection.close();

    }

}
