package com.sun.health.newwork.rabbitmq.work_model;

import com.rabbitmq.client.*;
import com.sun.health.newwork.rabbitmq.util.RabbitMQConnectionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static com.sun.health.newwork.rabbitmq.work_model.WorkModelConfig.*;

public class WorkModelReceiver {

    private Logger logger = LoggerFactory.getLogger(WorkModelReceiver.class);

    @Test
    public void testReceive1() throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitMQConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        // 每次从队列中获取的数量
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                logger.info("1 WorkModelReceiver receive: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    channel.abort();
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false); // 手动回信
                }
            }
        };
        TimeUnit.SECONDS.sleep(1);
        channel.basicConsume(WORK_MODEL_QUEUE, false, consumer);
        TimeUnit.SECONDS.sleep(60);
        channel.close();
        connection.close();
    }

    @Test
    public void testReceive2() throws InterruptedException, IOException, TimeoutException {
        Connection connection = RabbitMQConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        // 每次从队列中获取的数量
        channel.basicQos(1);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                logger.info("2 WorkModelReceiver receive: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    channel.abort();
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(), false); // 手动回信
                }
            }
        };
        TimeUnit.SECONDS.sleep(1);
        channel.basicConsume(WORK_MODEL_QUEUE, false, consumer);
        TimeUnit.SECONDS.sleep(60);
        channel.close();
        connection.close();
    }

}
