package com.sun.health.newwork.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnectionUtils {

    private static final String HOST = "localhost";
    private static final int PORT = 5672;
    private static final String USERNAME = "springcloud";
    private static final String PASSWORD = "123456";
    private static final String VIRTUAL_HOST = "/";

    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername(USERNAME);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setVirtualHost(VIRTUAL_HOST);
        return connectionFactory.newConnection();
    }

}
