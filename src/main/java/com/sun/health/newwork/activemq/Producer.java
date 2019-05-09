package com.sun.health.newwork.activemq;

/**
 * Created by 华硕 on 2019-05-09.
 */
public interface Producer {

    void sendToQueue();

    void sendToTopic();
}
