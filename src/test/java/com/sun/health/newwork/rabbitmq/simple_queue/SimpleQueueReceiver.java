package com.sun.health.newwork.rabbitmq.simple_queue;

import com.rabbitmq.client.*;
import com.sun.health.newwork.rabbitmq.util.RabbitMQConnectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.sun.health.newwork.rabbitmq.simple_queue.SimpleQueueConfig.*;

public class SimpleQueueReceiver {

    private Logger logger = LoggerFactory.getLogger(SimpleQueueReceiver.class);

    @Test
    public void testReceive() throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitMQConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
//        channel.queueDeclare(SIMPLE_QUEUE_NAME, false, false, false, null);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                super.handleDelivery(consumerTag, envelope, properties, body);
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                logger.info("routingKey: " + routingKey);
                logger.info("contentType: " + contentType);
                logger.info("deliveryTag: " + deliveryTag);
                logger.info("SimpleQueueReceiver receive: " + new String(body));
                channel.basicAck(deliveryTag, false);
            }
        };
        channel.basicConsume(SIMPLE_QUEUE_NAME, true, defaultConsumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }

}
