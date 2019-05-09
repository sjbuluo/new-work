package com.sun.health.newwork.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by 华硕 on 2019-05-08.
 */
public class SimpleActiveMQTest {

    private Logger logger = LoggerFactory.getLogger(SimpleActiveMQTest.class);

    @Test
    public void testActiveMQProducerQueue() {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://47.105.97.246:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("test_queue");
            producer = session.createProducer(queue);
            TextMessage textMessage = session.createTextMessage("hello! test_queue");
            producer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testReceive() {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://47.105.97.246:61616");
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("test_queue");
            consumer = session.createConsumer(queue);
            consumer.setMessageListener((Message message) -> {
                if(message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        logger.warn("接受{}", textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.warn("接收不到非文字信息");
                }
            });
            System.in.read();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(consumer != null) {
                try {
                    consumer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testTopicSend() {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://47.105.97.246:61616");
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("test_topic");
            producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage("Send to test_topic from producer");
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if(producer != null) {
                try {
                    producer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    public void testTopicReceive() {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://47.105.97.246:61616");
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("test_topic");
            consumer = session.createConsumer(topic);
            consumer.setMessageListener((Message message) -> {
                if(message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        logger.warn("接受: {}", textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.warn("无法接受非文字消息");
                }
            });
            System.in.read();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
